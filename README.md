# 결과

- MSA-Instance-Product
    - CRUD API
    - Multi Read API (api/product?ids=1,2,3)
- MSA-Instance-Order
    - Eureka, Ribbon 사용하지 않는(product service와 통신하지 않는) CRUD API
    - Eureka, Ribbon을 사용한 Create API 
        - Order Create 요청에 포함된 LineItem 정보에서 productId를 추출
        - productId를 기준으로 /api/product/productId 조회
        - 200을 받은 경우 Create 수행
        - 200을 받지 못한 경우 Error Handling
- MSA-Instance-Eureka
    - 기초적인 Eureka 설정
    - ![eureka-self-preservation](/assets/images/eureka-self-preservation.jpg) 
    - eureka의 self-preservation에 대해서는 아직 깊이 파악하지 못함

# 진행과정

1. DB Schema 간소화
    - ![db-schema](/assets/images/db-schema.jpg)  
    * 테이블 이름 변경(Order -> OrderGroup)
1. 도메인 분리
    - MSA-Instance-Product : Product Entity
    - MSA-Instance-Order   : OrderGroup, LineItem
1. 상호 참조
    - OrderGroup Entity
        ```java
        public class OrderGroup {
            ... 

            @JsonManagedReference
            @OneToMany(mappedBy = "orderGroup", cascade = CascadeType.ALL)
            private List<LineItem> lineItems;
        ```
    - LineItem Entity 
        ```java
        public class LineItem implements Serializable {
            ...

            private Long productId;

            @JsonBackReference
            @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "order_group_id")
            private OrderGroup orderGroup;
        ```
    - 영속성 관리 : CascadeType.ALL
1. Entity Read할 때 JSON 응답의 무한 순환 참조 대응
    1. @JsonIgnore 
        - annotaged property를 serialize와 deserialize에서 제외하여 해결
    1. @JsonManagedReference, @JsonBackReference
        - 무한 순환 참조 문제를 해결하기 위해 도입
        - @JsonManagedReference : Parent 쪽에 적용. serialize와 deserialize 과정이 정상적으로 진행됨
        - @JsonBackReference : Child 쪽에 적용. serialize와 deserialize 과정에서 `Managed`의 reference가 저장됨
1. Entity Save할 때 INSERT 전에 SELECT하는 이유 확인
    - [참조][7]
1. Entity와 DTO의 분리
    - view에서 표현하는 속성값들이 요청에 따라 계속 변화할 수 있음
    - entity의 속성값에 변화를 주면 영속성 모델의 순수성이 모호해짐
    - DB Layer와 View Layer 사이의 역할을 분리
    * 질문 : DTO를 얼마나 나누어야하는가? requestDto/responseDto 분리? method 마다 분리?
1. @ResponseBody 동작 과정 이해
    - POST
        - HttpRequest -> Object 변환
        - 내부적으로 Jackson2HttpMessageConverter와 ObjectMapper가 사용됨
    - GET
        - QueryParameter -> Object 변환
        - WebDataBinder의 Setter가 사용됨
1. RestAPI CRUD의 return type convention 확인
    - Create : id or object
    - Read : object
    - Update : updatedObject
    - Delete : nothing
1. RestAPI의 Error Handling
    - @ExceptionHander 
        - Controller 마다 적용되며 Application 전체에 적용되지 못함
        - BaseController에 적용하고 상속하는 방법이 있으나, 다른 JAR에 있는 Controller에는 적용할 수 없음
    - @HandlerExceptionResolver
        - Application에서 thrown되는 error 기반(Controller에 국한되지 않음)
        - 4**, 5**에러의 경우 ResponseBody에는 어떤 정보도 포함할 수 없음
        - 실패의 원인을 Body에 담아서 알려주어야하는 필요성 존재
    - @ControllerAdvice
        - Application에서 thrnwon되는 error 기반
        - Body에 Eror 정보 포함 가능
1. RestTemplate의 Error Handling
    - RestTemplate이 exception을 throw하는 경우
        - HttpClientErrorException – in case of HTTP status 4xx
        - HttpServerErrorException – in case of HTTP status 5xx
        - UnknownHttpStatusCodeException – in case of an unknown HTTP status
    - ClientHttpResponse를 활용한 StatusCode 별 흐름 분기 적용
1. Eureka Ribbon 이해
1. IntelliJ Idea Application 여러 개 띄우기

# 2주차 리뷰

1. 먼저 쉽게 작성하자.
    > 1차선 먼저 뚫고 점점 넓히자. 처음부터 4차선 8차선 힘들다.
1. Product Server의 Response가 String으로는 받아지지만, Header\<ProductApiResponse\>로 받아지지 않는 문제
    - List\<ProductApiResponse\>로 받는 부분을 ResponseEntity에서 Header와 Body를 나누어서 생각해보자.
    - 우선 ProductApiResponse를 먼저 받도록 한 뒤, 나중에 추가적으로 확장하자.
    - restTemplate의 동작 과정에 대해서 더 파악하자.
    > Entity name이 Order로 되어있어 문법이 모호해지는 문제 해결
    > restTemplate의 내부 동작 과정 이해
1. interceptor를 적용하는 지점에 대해서 파악하기
    - interceptor와 auditor의 차이점에 대해서 파악하기
    - interceptor는 공통관심사에 대해서 횡단으로 처리하는 범위
    > LoadBalanced interceptor를 통해 restTemplate이 intercept 됨을 파악
    > [WIP] Ribbon interceptor의 join point 확인
1. stream의 경우 single/multi core에서 어떻게 동작하는지 파악하기   
    >
1. transactional에 대해서 파악하기
    > transaction 내에서 영속성 콘텍스트가 동작하는 과정 파악
1. MSA-Instance-Product GET에서 POST로 변경하기
    > [WIP] 미적용
1. Test 코드 
    - @Before를 통해서 공통 기능을 실행 전에 수행한다.
    - MSA-Instance-Product에 직접 요청을 날리면 의미가 없다. MockServer를 사용하자.
    > [WIP] 미적용

# 학습 내용

## RestTemplate 동작 과정

- 통신 방법의 변화
    1. URLConnection : 타임아웃, 쿠키 제어 불가능
    1. HttpClient(HttpComponent) : 응답코드마다 별도의 로직이 필요
    1. RestTemplate : non-blocking, async 처리 불가능
    1. WebClient : Spring 5에서 지원

![how-resttemplate-works](/assets/images/how-resttemplate-works.jpg) 

- 내부 동작 과정
    1. Application : RestTemplate 객체 생성 및 method 호출
        - RestTemplate 생성자의 인자 두 가지
            - ClientHttpRequestFactory
            - HttpMessageConverter
        ```java
        package org.springframework.web.client;
        ...
        public class RestTemplate ... {
            // 수 많은 HttpMessageConverter 등록
            public RestTemplate() {
                this.messageConverters.add(new ByteArrayHttpMessageConverter());
                this.messageConverters.add(new StringHttpMessageConverter());
                ...
            }
            // ClientHttpRequestFactory 등록
            public RestTemplate(ClientHttpRequestFactory requestFactory) {
                this();
                setRequestFactory(requestFactory);
            }
            // MessageConterver 등록
            public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
                validateConverters(messageConverters);
                this.messageConverters.addAll(messageConverters);
                this.uriTemplateHandler = initUriTemplateHandler();
            }
        ```
    1. RestTemplate : HttpMessageConverter를 사용해서 RequestEntity -> JSON 변환
    1. RestTemplate : ClientHttpRequestFactory로부터 ClientHttpRequest 생성; 
    1. ClientHttpRequest : 변환된 JSON와 HTTP를 사용해서 서버와 통신
    1. RestTemplate : 오류 발생시 ResponseErrorHandler 사용
    1. ResponseErrorHandler : ClientHttpReponse -> JSON Error 응답 메시지 반환
    1. RestTemplate : HttpMessageConverter를 사용해서 JSON -> ResponseEntity 변환

## LoadBalanced RestTemplate 동작 과정

- Enable load balancing
    ```java
    @Bean
    @LoadBalanced //Enable load balancing
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    ```  
- @LoadBalanced
    - @Target : Annotaion이 적용되는 위치를 명시
        - Field, Parameter, method에 @LoadBalanced를 사용할 수 있음을 의미
    - @Retention : Annotation이 영향을 미치는 범위
        - RetentionPolicy.SOURCE : compiler에 의해 버려짐
        - RetentionPolicy.CLASS : compiler까지는 유지되지만, VM runtime에는 유지될 필요 없음
        - RetentionPolicy.RUNTIME : compiler에서 유지되고, VM runtime에도 유지됨
    - @Documented :
    - @Inherited : SubClasses에 Annotation이 상속되도록 설정(Default : Annotation은 상속되지 않음)
    - @Qualifier : 동일한 타입의 Bean을 구분하는데 사용(SubClass에서 사용될 것으로 추측)
    ```java
    package org.springframework.cloud.client.loadbalancer;
    ...
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @Qualifier
    public @interface LoadBalanced {
    }
    ```
- spring-cloud/spring-cloud-commons
    - @Configuration(proxyBeanMethods = false) : Bean Method proxy 미적용
    - @ConditionalOnClass : 해당 클래스가 classpath에 존재하는 경우에만 설정을 활성화
        - 개발자가 특정 dependency를 classpath에 추가하는 경우 활성화된다.
    - @COnditionalOnBean : 해당 Bean이 classpath에 존재하는 경우에만 설정을 활성화
    - @EnableConfigurationProperties : @ConfigurationProperties가 설정된 Bean을 등록한다.
    ```java
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RestTemplate.class)
    @ConditionalOnBean(LoadBalancerClient.class)
    @EnableConfigurationProperties(LoadBalancerProperties.class)
    public class LoadBalancerAutoConfiguration {
    @LoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();

    @Bean
    public SmartInitializingSingleton loadBalancedRestTemplateInitializer( final List<RestTemplateCustomizer> customizers) {
        return new SmartInitializingSingleton() {
            @Override
            public void afterSingletonsInstantiated() {
                for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
                for (RestTemplateCustomizer customizer : customizers) {
                    customizer.customize(restTemplate);
                }
                }
            }
        };
    }

    @Configuration
    @ConditionalOnClass(RetryTemplate.class)
    public static class RetryInterceptorAutoConfiguration {
            // restTemplate 마다 loadBalancerInterceptor를 추가
            @Bean
            @ConditionalOnMissingBean
            public RestTemplateCustomizer restTemplateCustomizer(
                final RetryLoadBalancerInterceptor loadBalancerInterceptor) {
                return new RestTemplateCustomizer() {
                    @Override
                    public void customize(RestTemplate restTemplate) {
                        List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                                restTemplate.getInterceptors());
                        list.add(loadBalancerInterceptor);
                        restTemplate.setInterceptors(list);
                    }
                };
            }
        }
    }

    // Ribbon dependency에 의해서 추가되는 Interceptor
    // restTemplate에 추가되어서 적용된다. 
    @Bean
    public LoadBalancerInterceptor ribbonInterceptor(
        LoadBalancerClient loadBalancerClient,
        LoadBalancerRequestFactory requestFactory) {
    return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
    }
    ```




## Spring Cloud Netflix 

![netflix-oss-and-spring-cloud](/assets/images/netflix-oss-and-spring-cloud.jpg)  

- 'Netflix OSS'와 'Spring Cloud'의 교집합
    - Netflix OSS
        - MSA 플랫폼 Solutions
        - Netflix 프로덕션에서 검증된 코드
    - Spring Cloud 
        - 개발자들에게 익숙한 Spring Boot 기반
    - Hystrix / Eureka / Ribbon / Zuul

### 구조

- Zuul                      // API GateWay
    - Hystrix Command       // Circuit Breaker
        - Ribbon Client     // Client Load Balancer
            - Eureka Client // Service List 
            - HttpClient    // RestTemplate

### Zuul

- API Gateway
- MSA 환경에서 API GateWay의 필요성
    - Single EndPoint 제공
        - API를 사용할 Client들은 API GateWay 주소만 인지
    - API의 공통 로직 구현
        - Logging, Authentication, Authorization
    - Traffic Control
        - API Quota, Throttling

### Hystrix

- Netflix가 만든 Fault Tolerance Library
    - 장애 전파 방지 & Resilience(회복 탄력적)
- 주요 기능 4가지
    1. Circuit Breaker
        - 조건에 따라 Circuit Open 여부 결정
    1. Fallback
        - 조건에 따라 원본 method 대신 Fallback method 실행
    1. Isolation
        - Circuit Breaker 별로 Semaphor 또는 Thread Pool 방식의 Isolation 적용  
    1. Timeout
        - hystrix로 감싼 메서드들은 일정시간이 지나면 Exception 발생
- 적용 방법
    1. Spring Cloud Dependency 추가
    1. Spring Cloud 없이 Hytrix만 사용하는 방법
        - @HystricxCommond Annotation를 사용한 구현 
        - HystrixCommand 상속하여 구현

#### Circuit Breaker

- 기능
    - method에서 exception이 발생한 경우를 통계를 낸다.(exception 횟수 / 실행 횟수)
    - 일정시간 동안 일정 개수 이상의 호출이 발생한 경우, 일정 비율 이상의 에러가 발생하면 -> Circuit Open(호출차단)
        - Circuit Open이 되면 메서드를 호출했을 때, 바로 exception이 발생해서 호출된 method의 body가 실행되지 않는다. 
        - Default : 10초간 20개 이상의 호출이 발생 한 경우, 50% 이상의 에러가 발생하면 5초간 Circuit Open. [property settings][2]
    - 일정시간 경과 후에 단 한개의 요청에 대해서 호출을 허용하며(Half Open), 이 호출이 성공하면 -> Circuit Close(호출허용)
- 적용
    - 통계의 단위는 instance와 instance 내부의 circuit breaker 단위가 적용된다.
    - 앱 실행시 여러 개의 CircuitBreaker instance가 생성된다. method 별로 어떤 CircuitBreaker를 사용할 것인지 지정할 수 있다.
        - @HytrixCommand(commandKey = "ExtDep1") [참조][3]
    - 여러 메서드가 동일한 API 서버에 요청을 보내는 경우, 같은 CircuitBreaker를 지정할 수 있다.
    - 비즈니스 로직을 개발한 개발자가, 동일한 Dependency를 갖는 Hystrix Command들을 동일한 CommandKey로 묶는 것이 중요하다.  

#### FallBack

- 아래의 경우가 발생했을때 Fallback으로 지정된 메소드는 원본 메소드 대신 실행된다.
    1. Circuit Open
    1. Any Exception(HytrixBadReqeustException 제외)
    1. Semphore / ThreadPool Rejection (Thread Isolation에서 설명)
    1. Timeout
- 지정방법
    - @HytrixCommand(commandKey = "ExtDep1", fallbackmethod = "recommendFallback") [참조][4]
- 아래의 경우에는 HytrixBadReqeustException로 감싸서 에러를 처리해야 한다.
    1. illegalargumentexception(메서드가 부적절한 인자를 받은 경우, null인자 포함)
    1. IllegalStateException(메서드가 부적절할 때 호출된 경우)
- HytrixBadReqeustException의 경우 Circuit Open의 통계에 포함되지 않는다. API Caller가 잘못한 경우이기 때문

#### Isolation

- 두 가지 Isolation 방식을 Circuit Breaker 별로 지정 가능
    - Semaphore 
        - Circuit Breaker : Semaphore = 1 : 1
        - Circuit Breaker 뒷단의 서버에 들어갈 수 있는 요청의 수를 제한(Ex. Semaphore max = 10)
        - 최대 요청 수를 넘으면 Fallback 실행 
        - Command를 호출한 Caller Thread에서 Method 실행.(Semaphore를 acquire하고 수행)
        - Timeout이 제 시간에 발생하지 못함. 
    - Thread(Default)
        - Circuit Breaker : ThreadPool  = 1..N : 1
        - Command를 호출한 Thread가 아닌 Thread Pool에서 메소드 실행

#### Timeout

- Circuit Breaker 단위로 적용되며 기본값은 1초이다.
- API의 경우 1초 이내에 동작해야하지만 UI 등의 경우 1초를 충분히 넘을 수 있다. 수정하지 않으면 모든 요청이 Timeout이 발생한다.

### Ribbon

- Netflix가 만든 Software Load Balancer를 내장한 IPC Library
    - Client Load Balancer with HttpClient
    - API Caller 쪽에 Load Balancer를 장착하기 때문에 Client Load Balancer
- LoadBalancer라는 개념을 완벽하게 프로그래밍 할 수 있게 된다.  
- H/W L4나 AWS의 ELB의 경우 Load Balancer 하나의 주소를 알고 그 쪽으로만 요청을 보낸다.
- H/W가 아닌 S/W가 서버 목록을 받아서 번갈아가며 호출하자
- SpringCloud에서 RibbonClient를 직접 호출해서 사용하는 경우는 존재하지 않는다.
    1. Spring Cloud Zuul은 Ribbon을 내장해서 직접 Load Balance를 수행
    1. Spring Cloud가 Enable 되어있고 + RestTemplate이 Bean으로 등록된 경우, @LoadBalanced를 붙히면 LoadBalancer의 intercepter를 통해 LB를 껴넣는다. 즉, restTemplate를 호출하는데 알아서 LB를 수행한다. 
    1. restTemplate에 param으로 주는 서버 주소는 ip와 port를 지우고, '호출할 서버군'의 이름을 넣으면 적절한 서버를 호출한다. 

### Eureka

- Eureka Server <- ip:port 등록 Eureka Client
- Eureka Server <- ip:port 조회 Eureka Client
- Spring Application의 LifeCycle과 함께 맞물려서 동작한다.
    - spring.application.name : 서버군 이름 
    - Server 시작지 Eureka 서버에 자동으로 자신의 상태(서버군)을 등록(UP)
    - 주기적 HeartBeat로 Eureka Server에 자신이 살아 있음을 알림
    - Server 종료 시 Eureka 서버에 자신의 상태 변경(DOWN) 혹은 자신의 목록 삭제

### Eureka + Ribbon

- Ribbon이 생성될 때 Eureka가 있다면, Eureka를 구성하는 7가지의 Bean 중에서 두 가지가 변경된다.
    - ServerList\<Server\>
        - 기본 : ConfigurationBasedServerList (application.yaml에 설정된 목록)
        - 변경 : DiscoveryEnableNIWSServerList(eureka에서 서버 목록을 가져옴)
    - IPing 
        - 기본 : DummyPing(아무거도 안하던 핑)
        - 변경 : NIWSDiscoveryPing(Eureka에 잘 등록되어 있는지 확인하는 핑)
    - 서버의 목록을 설정으로 명시하는 대신 Eureka를 통해서 Look Up해오는 구현으로 변경


## JPA 영속성 관리

1. EntityManagerFactory를 통해 EntityManager 생성
1. EntityManager는 entity의 저장,수정,삭제,조회 등의 작업 수행
1. entityManager.persist(entity)는 entity를 `영속성 컨텍스트`에 저장
1. entityManager.flush()는 `영속성 컨텍스트`에 저장된 정보를 DB에 반영
    - transaction commit(), JPQL 쿼리 실행시 flush() 자동 호출
1. 영속성 컨텍스트 장점
    - 1차 캐시 : entity 조회시 영속성 컨텍스트에 존재하면 바로 return, 아니면 DB 조회 후 return
    - 동일성 : 항상 같은 entity instance를 return(instance의 주속밧이 같음)
    - 트랜잭션을 지원하는 쓰기 지연(Transactional write-behind): 트랜젝션 커밋 될때까지 내부 쿼리저장소에 모아뒀다가 한번에 실행
    - 변경감지(Dirty Checking): 엔티티의 스냅샷을 유지하면서 엔티티의 변경사항을 체크한다.
    - 지연로딩(Lazy Loading): 연관된 엔티티를 모두 불러오는 것이 아니라, 실제 호출될때 로딩되도록 지원(프록시 객체 사용)
1. Entity CRUD
    - Create
        - em.persist(entity)로 영속성 컨텍스트에 저장
        - transaction commit 전 메모리에 SQL 유지
        - transaction commit 후 DB에 일괄 반영
    - Read
        - em.find(entity)로 1차 캐시 조회 후 DB 조회
        - DB에서 조회된 entity는 영속성 컨텍스트에서 관리
    - Update
        - 별도의 API가 없음. em.update(entity) 존재하지 않음
        - entity.setName("newName")등을 통해 entity 정보를 변경
        - transaction commit시 변경감지를 통해 수정된 entity의 UPDATE SQL 생성 후 반영
    - Delete 
        - em.remove(entity)
        - 영속성 컨텍스트에서 바로 삭제
        - transaction commit 될때가지 쓰기 지연
1. em.detach()?
    - em.detach(entity)
    - entity를 준영속 상태로 변경
    - 영속성 컨텍스트에서 저장되었다가 분리된 상태
    - 영속성 컨텍스트에서 지운 상태
1. em.merge()?
    - 준영속 상태의 entity를 영속성 컨텍스트에 저장
1. `Transaction-scoped Persistence context`과 `Extended Persistence context`
    - 차이점 : [참조][5]
1. self join 사용할 때 주의점
    - 연관 엔티티가 DB에 등록된 키값을 가지고 있다면 detached entity passed to persist Exception이 발생한다?
    - 아직 이해할 수 없는 내용 [참조][6]


## @Configuration, @Component 차이점

- @Component
    - 개발자가 직접 작성한 클래스를 Bean으로 등록하는 경우
    ```java
    @Component
    class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
            // 생략
        }
        @Override
        public void handleError(ClientHttpResponse httpResponse) throws IOException {
            // 생략
        }
    }
    ```
- @Configuration + @Bean
    - 외부라이브러리 또는 내장 클래스를 Bean으로 등록하는 경우
    ```java
    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {
        // 생략
        @Bean
        public Docket commonApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    // 생략
                    .build();
        }
    }
    ```

## @Configuration(proxyBeanMethods = false)

- 사용자 코드에서 @Bean method를 직접 호출하는 경우에도, Bean lifecycle 동작을 적용하기 위해 @Bean 메서드를 proxy화할 것인지 여부
    - proxyBeanMethods = true인 경우(default)
        - 모든 Bean은 life cycle에 따라서 동작하며 기본적으로 singleton으로 동작한다.
        - 사용자 코드에서 @Bean method를 직접 호출하는 경우에도 intercepted되고 singleton 객체가 return된다.
        - 이 과정은 proxy interceptor에 의해서 수행된다.
    - proxyBeanMethods = false인 경우
        - 현재의 기능을 여러 system에 도입하고 싶은 경우가 있다. 현재의 architecture와 기능은 유지하고, code를 replicate한 뒤 약간의 text transformation과 normalization만 진행하면 된다. 하지만 이 경우 library가 제공하는 기능에 비해서 configuration이 너무 많이 존재하게 된다. 여러 data source에서 data를 fetch하고, 각 system으로의 변형 과정이 필요하기 때문인다. 이를 개선하기 위해서 '기본적인 경우에는 default로 구현되어 있고, 필요한 경우 override해서 사용할 수 있는' configuration이 필요하다. spring의 autoconfiguration이 이 역할을 수행하고, autoconfiguration들은 `proxyBeanMethods = false`로 설정되어 있다.
    - 의존객체를 사용하거나 주입할 때, 일반적인 configuration의 경우 @Bean method를 직접 호출한다. proxy에 의해서 singleton 객체가 return되는 것이 보장되기 때문이다. 하지만 AutoConfiguration은 @Bean method를 호출하지 않고 @Autowired를 통해서 주입을 진행한다. @Bean method를 직접 호출하지 않기 때문에 `proxyBeanMethods = false`로 설정할 수 있다.
    - @Bean method를 직접 호출하지 않는 경우만 `proxyBeanMethods = false`를 사용하는 것이 좋다. 대부분의 configuratino에서도 false를 사용한다.
        ![proxy-bean-methods-false](/assets/images/proxy-bean-methods-false.jpg) 


# TODO

- objectMapper(Entity2Dto)
- Serializable
- SpringFramework 5
    - WebFlux stack
    - WebClient


[2]: https://youtu.be/J-VP0WFEQsY?t=872
[3]: https://youtu.be/J-VP0WFEQsY?t=914
[4]: https://youtu.be/J-VP0WFEQsY?t=1033
[5]: https://stackoverflow.com/questions/2547817/what-is-the-difference-between-transaction-scoped-persistence-context-and-extend
[6]: http://chomman.github.io/blog/java/jpa/programming/jpa-cascadetype-%EC%A2%85%EB%A5%98/
[7]: https://kapentaz.github.io/jpa/Spring-Data-JPA%EC%97%90%EC%84%9C-insert-%EC%A0%84%EC%97%90-select-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0/#