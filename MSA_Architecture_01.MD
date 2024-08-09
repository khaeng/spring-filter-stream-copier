	
MSA의 논리 아키텍처(inner Architecture 중심)  MSA / SW Engineer의 인생   
2019. 4. 22. 0:21
복사https://blog.naver.com/stmshra/221519347649
﻿필자는 Application Architecture 중 주로 물리보다는 논리적인 아키텍처링 설계 및 기술 리딩을 

전문으로 해 왔다.(일부에서는 Application Modeler, 혹은 data modeler로 불려지기도 했다)



따라서 MSA에서도 가트너에서 의미하는 MSA 기술 컴포넌트의 분류 상 auter보다는 Inner 

아키텍처에 많은 관심 및 프로젝트를 리딩하고 있다.



[참고: 가트너의 Inner architecture - 짙은 파란색 컴포넌트와 서비스 정의 영역]
<img src="https://blogfiles.pstatic.net/MjAxOTA0MjFfMzgg/MDAxNTU1ODQ4NTEwNjk5.WO3-9U3zwffPfSnHJx6ZPduL3tm-OBEmCBwG_f124hQg.kyjqgtLn7x50nSvJFpjXSup8ZxSBZV0Of4W4MxzF0ukg.PNG.stmshra/%EA%B0%80%ED%8A%B8%EB%84%88_MSA_Component.PNG?type=w2"/>
﻿

﻿위의 컴포넌트 중 Inner Architecture에 해당하는 서비스 정의 및 프레임워크의 논리 layer에 대한 

구조를 정의하는 영역을 의미한다.



위와 같이 MSA의 많은 구성요소가 있지만 특히 Inner에 해당하는 것 중 분석/설계 시 고려해야 하는 

영역은 아래와 같다.

○ 서비스 정의 : 서비스를 도출하고 이를 나누고 로직을 정의하기 위한 영역

○ 프레임워크 : MSA 어플리케이션을 구현하기 위해서 논리적인 컴포넌트의 Layer 및 역할자 정의

○ API : 내부 및 외부 서비스 연계   



이 중 서비스 정의는 "Bounded Context찾기"와 "서비스 평가하기"에 언급을 하였으므로 이번

글에서는 논리적인 컴포넌트 Layer와 내부/외부 서비스 연계를 하는 논리적인 컴포넌트에 대하여

이야기 해보고자 한다.



일반적으로 분석이 끝나고 설계 특히 UML 설계를 하려면 컴포넌트의 레이어가 반드시 정의

되어야 한다.이때 중요한 것이 논리 아키텍처이다.



다음은 필자가 생각한 간단한 MSA를 적용한 논리 아키텍처 예를 정의한 것이다.


<img src="https://blogfiles.pstatic.net/MjAxOTA0MjJfMTg5/MDAxNTU1ODU5MTc3OTA0.GKbvNMoZZ77NX7FH-EAKsDVTehq-O1ctjvxl8sRuDmEg.p9U5JiHDlV74juNXFt76-f6Zzj0rBbzTNwqgd6pnMc4g.PNG.stmshra/%EB%85%BC%EB%A6%AC%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.PNG?type=w2"/>



 다시 한번 이야기 하지만, 위의 논리 아키텍처는 하나의 사례이며, 이는 각 프로젝트 혹은

시스템마다 다르게 정의될 수 있다.



이렇게 각 레이어별로 역할을 정의하면 이를 지원하는 프레임워크는 이를 물리적으로 구현하고

설계자들은 해당 컴포넌트들의 역할 대로 구현체를 만들어야 한다.(아니면 솔루션 활용)



MSA에서는 각 서비스별로 아키텍처를 다르게 하고, 해당 서비스별로 가장 알맞은 아키텍처를

구현하고 수립해야 하며, 표준화된 프레임워크 또한 별로 의미가 없다고 이야기 한다.

그러나, 이미 SWA와 개발자의 역할이 분리되어 있는 한국의 개발 환경 특성상 표준으로 사용하는

프레임워크는 정의가 되어져야 할 듯 하다.



MSA 기술을 포함한 프레임워크는 어떤 것이 있을까?

일반적으로 많이 이야기하는 프레임워크는 주로 Spring Boots가 많이 회자되고 있다.



필자가 생각하는 MSA 프레임워크가 반드시 가져야 하는 기능은 아래 3가지 정도가 아닐까 한다

① Join이 어려우니 DTO혹은 VO의 Merge 기능(각 영역에서 읽어온 메시지를 동시에 merge할 수

   있는 기능

② 보상트랜잭션을 관리하는 Saga Pattern 적용

③ 내부 서비스 간의 연계를 처리하는 mediation 역할을 하는 컴포넌트



물론 그 외 여러 패턴 별로 다 기능을 가지고 있다면 좋겠지만, 그것은 각 현장에서 적용하는 

것이 ROI가 있을 듯 하지만....



위의 논리 아키텍처를 구현할 수 있는 물리적인 아키텍처는 이미 많은 글이 있으니 언급을 안하려고 

한다. 다음은 조직에 대하여 논할까 한다

[출처] MSA의 논리 아키텍처(inner Architecture 중심)|작성자 리니 LGCNSer         

   


N-tier 아키텍처
<img src="https://velog.velcdn.com/images/whattsup_kim/post/0e553724-60cb-42fb-baff-72d0a470fc8a/image.png"/>

N계층 아키텍처는 애플리케이션을 논리적 레이어와 물리적 계층으로 나눈다.
레이어는 책임을 구분하고 종속성을 관리하는 방법이다.
레이어마다 특정 책임이 있음
상위 레이어는 하위 레이어의 서비스를 사용할 수 있지만 하위 레이어는 상위 레이어의 서비스를 사용할 수 없음
N계층 아키텍처는 폐쇄형 레이어 아키텍처 또는 개방형 레이어 아키텍처를 사용할 수 있다.
폐쇄형 : 자신과 맞닿아있는 하위 레이어로만 접속이 가능
개방형 : 하위에 있는 모든 레이어로 접속이 가능
계층은 물리적으로 분리되어 별도의 시스템에서 실행된다.
계층은 다른 계층을 직접 호출하거나 비동기 메시징(메시지 큐)을 사용할 수 있음.
계층을 물리적으로 분리하면 확장성과 복원력이 향상되지만 추가 네트워크 통신으로 인해 대기 시간도 증가함.
예를 들어, 3계층 애플리케이션에는 프레젠테이션 계층, 중간 계층(비즈니스 논리 처리), 데이터베이스 계층이 있음(중간 계층은 선택 사항).
전통적인 비즈니스에서 많이 사용되던 아키텍처이지만, 모노로틱(내부 요소 간 의존성이 강함)한 특성때문에 최근에는 조금 지양되고 있다.   


Web-Queue-Worker 아키텍처
<img src="https://velog.velcdn.com/images/whattsup_kim/post/972b8ea9-8d33-414d-94d4-45f1ca5d689b/image.png"/>



애플리케이션의 웹 프론트엔드는 HTTP 요청을 처리하고 백엔드 작업자(worker)는 CPU 집약적인 작업이나 장기 실행 작업을 수행한다. 이때, 프론트엔드는 비동기 메시지 큐(Queue)를 통해 작업자(worker)와 통신한다.
Web-Queue-Worker 아키텍처는 보통 다음과 같은 구성을 갖고 있다.
하나 이상의 데이터베이스
빠른 읽기를 위해 데이터베이스의 값을 저장하는 캐시
정적 콘텐츠를 바로 제공하기 위한 CDN(Content Delivery Network)
원격 서비스(ex. 전자 메일 또는 SMS 서비스)
인증을 위한 Identity Provider
웹과 워커는 둘 다 상태 비저장이다.
세션 상태는 분산된 캐시에 저장할 수 있다.
모든 장기 실행 작업은 작업자에 의해 비동기적으로 수행된다.
작업자는 큐의 메시지에 의해 동작(trigger)되거나, 일정에 따라 실행된다.
(장기 실행 작업이 없는 경우 작업자를 생략할 수 있음)
프론트엔드는 웹 API 형태로 구성될 수 있다.   


마이크로 서비스 아키텍처
<img src="https://velog.velcdn.com/images/whattsup_kim/post/218ea71a-2899-4135-9fe8-e59c8f80046f/image.png"/>


모든 요소를 하나의 애플리케이션에 구축하는 전통적인 모놀리식 접근 방식 대신, 마이크로 서비스는 작고, 독립적이며, 느슨하게 결합되어 있다.
각 서비스는 소규모의 집중 개발 팀에서 구축할 수 있으며, 개별 서비스를 배포할 때 팀 간의 조정이 거의 필요 없으므로 업데이트를 자주 수행할 수 있다(서비스를 독립적으로 배포할 수 있음).
서비스가 잘 정의된 API를 사용하여 서로 통신하며, 각 서비스의 내부 구현 세부 정보는 다른 서비스에서 볼 수 없다.
다중저장소 프로그래밍을 지원한다(모든 서비스가 동일한 기술 스택을 공유할 필요가 없음).
마이크로 서비스는 빠른 릴리스, 빠른 개발, 빠른 혁신, 복원력이 있는 아키텍처이다. 하지만 마이크로 서비스 아키텍처는 N-tier 아키텍처 또는 Web-Queue-Worker 보다 빌드 및 관리 방법이 좀 더 복잡함.
성숙한 개발 및 DevOps 문화가 필요함
관리/오케스트레이션, API 게이트웨이와 같은 요소가 추가적으로 필요


이벤트 기반 아키텍처
<img src="https://velog.velcdn.com/images/whattsup_kim/post/7410d868-606e-4826-b5c9-02b1a49bfc87/image.png"/>


이벤트 기반 아키텍처는 이벤트 스트림을 생성하는 이벤트 생성자 와 이벤트 를 수신하는 이벤트 소비자로 구성된다.
생산자는 소비자와 독립적 관계이며, 각 소비자는 서로 독립적 관계이다.
생산자는 수신자가 받던 안 받던 관계없이, 일단 이벤트를 채널에 보내둠.
이벤트가 발생하면, 해당 이벤트를 구독하는 사용자가 이벤트를 수신함(pub/sub 패턴).


pub/sub 패턴이란?
<img src="https://velog.velcdn.com/images/whattsup_kim/post/ea25578c-3f52-4bb0-8f56-735cb5c98c30/image.png"/>


- pub/sub 패턴은 비동기식 메세징 패턴으로,
-- Publisher(발신자)는 Subscriber(수신자)에 대한 정보를 몰라도 그냥 일단 메세지를 채널에 보내놓는다
-- 이 때 메세지에 맞는 Topic으로 보내놓으면, 해당 Topic을 구독중인 Subscriber에게만 메세지가 가게 된다


- 이벤트 기반 아키텍처는 지연 시간이 매우 짧은(실시간 처리) 어플리케이션 혹은 이벤트 요청에 다양한 종류의 작업을 처리해야 할 경우 유용하다.   
   
   
빅데이터 아키텍처
<img src="https://velog.velcdn.com/images/whattsup_kim/post/6702e98e-016c-44bd-b42e-99eb0d660194/image.png"/>


빅 데이터 아키텍처는 기존의 데이터베이스 시스템에 비해 훨씬 크고 복잡한 데이터의 수집, 처리 및 분석 등을 수행하도록 디자인되었다.
빅데이터 아키텍처는 MLOps에 대해 정리한 블로그에서 더 자세히 확인할 수 있다.
Reference

https://learn.microsoft.com/en-us/azure/architecture/guide/architecture-styles/
https://www.redhat.com/en/topics/cloud-native-apps/what-is-an-application-architecture
https://honglab.tistory.com/61
