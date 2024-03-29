# Kapt와 Ksp 차이
### Kapt
Kaspt는 Kotlin Annotation Processing Tool의 약자다.
command-line 도구이며, Kotlin 플러그인을 빌드한다.
빌드 프로세스 중에 Kotlin 코드의 어노테이션을 처리하는 데 사용된다. Gradle 및 Maven과 같은 도구와 함께 작동하며 어노테이션이 달린 코드를 분석하고 추가 코드를 생성한다.

#### Kapt keypoint
##### ✨ Anotiaiotn Processing
- Kapt의 주 목적인 코틀린의 어노테이션을 처리하는 것이다. 어노테이션은 컴파일러에 추가 정보를 제공하기 위해 코드 요소에 추가되는 메타데이터이다. kapt는 이런 어노테이션을 분석하는 책임을 지닌다.

##### ✨ Code Generations
- 어노테이션과 어노테이션된 요소를 기반으로 추가적인 코들린 및 자바 코드를 생성할 수 있다. 반복적인 작업을 자동화하므로 보일러플레이트(상용구) 코드를 줄일 수 있다.

##### ✨ Custom Annotations
- 커스텀 주석을 처리하고 개발자의 요구에 맞는 코드를 생성하도록 Kapt를 구성할 수 있다.

##### ✨ Use Cases
- 의존성 주입 프레임워크(hilt/Dagger, koin 등) 또는 직렬화/역직렬화 데이터 베이스 액세스 코드, API 클라이언트를 생성하여 고드 생성을 자동화함으로써 개발 프로세스 간소화
<br>

### Ksp
Ksp는 Kotlin Symbol Process의 약자이며, Kapt를 사용하는 기존 어노테이션 처리와 관련된 몇 가지 제한 사앙 및 문제를 해결하기 위해 등장한 프레임워크이다.
무엇보다 코틀린에서 어노테이션 처리의 성능과 편의성을 개선하기 위해 설계된 만큼, 빠른 성능을 보인다.

#### Ksp keypoint
##### ✨ Code Generations
- Ksp의 목표는 어노테이션 처리 성능을 향상시키는 것이다.

##### ✨ Performance
- Ksp는 코틀린 유형의 시스템 및 언어 기능을 활용하도록 설계되었다.
따라서 코틀린과 원활하게 작동한다.

##### ✨ Kotlin-DSL Integration
- Ksp는 Kotlin DSL과 잘 통합된다. 따라서 코틀린을 사용하여 코드 생성 로직을 더 쉽계 작성할 수 있으므로 코드가 더 깔끔해진다.

##### ✨ Improved Code Generation
- Ksp는 더 강력한 코드 생성 기능을 제공한다. 새로운 심볼과 유형을 생성할 수 있다. 이러한 방식으로 종속성 주입 및 직렬화와 같은 상황을 다양하게 처리할 수 있다.
<br>

### Kapt와 Ksp의 차이점

##### ‣ 성숙도(Maturity)
- Kapt는 더 오래되고 널리 사용되는 중이다(작성 일자인 24년 01월 기준).

##### ‣ 생태계(Ecosystem)
- Dagger2, Room과 같은 많은 코틀린 라이브러리가 Kapt와 함께 작동하도록 구축되어 있다. 이런 라이브러리를 사용하는 경우 아직까지는 Kapt가 권장된다.
- 본인도 얼마전 Hilt/Dagger를 Ksp로 사용하려했는데 안됬었음

##### ‣ 새로운 도구(New tool) 
- Ksp는 kapt의 한계점을 해결하기 위해 설계된 새로운 어노테이션 프로세싱 도구이다.

##### ‣ 성능(Perfomance)
- Ksp는 Kapt보다 2배 빠른 성능을 제공한다. 이는 Ksp가 더 효율적인 코드 생성 및 처리 속도를 가짐을 의미한다.
  
##### ‣ 직접 통합(Direct Integration)
- Ksp는 코틀린 컴파일러와 직접 통합되며, 코틀린 심볼과 API를 사용한다. 이를 통해 더 깔끔한 코드 생성을 가능하게 한다.

##### ‣ 호환성(Compatibility)
- Ksp는 아직 초기단계다. 프로젝트 요구사항이 Ksp로 충족되지 않거나, 모든 기존 라이브러리 및 빌드 시스템과 호환되지 않을 수 있다.


![Alt text](image.png)

<br>

### 결론
Ksp는 Kapt보다 빠르다. Ksp를 사용하면 빌드 시간을 단축할 수 있다. 프로젝트가 기존 프로젝트인 경우 Kapt를 사용하는 것이 더 쉬울 수 있다. 그러나 프로젝트가 최신 프로젝트인 경우 Ksp를 사용하는 것이 더 쉬울 수 있다. Ksp는 일부 라이브러리를 지원하지 않기 때문이다.