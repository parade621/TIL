# Atom18 
### 프로퍼티

프로퍼티(var, val)는 클래스의 일부분이며 점 표기법(객체 이름, 점, 프로퍼티 이름을 나열하는 방식)을 사용해야만 값에 접근 가능.
프로퍼티는 자신이 속한 객체의 상태를 표현한다.
맴버 함수는 점 표기법을 쓰지 않고(즉, 해당 프로퍼티를 한정하지 않고) 자신이 속한 객체의 프로퍼티에 접근 가능.
<br/>
```kotlin
class Cup2{
    var percentFull = 0
    val max = 100
    fun add(increase: Int): Int{
        percentFull += increase
        if(percentFull > max){
            percentFull = max
        }
        return percentFull
    }
}

fun main(){
    val cup =  Cup2()
    cup.add(50)
    println(cup.percentFull)
    cup.add(70)
    println(cup.percentFull)

}
```

<br/>


가변(immutable, var)는 최상위 프로퍼티로 선언 시 **안티패턴(anti-pattern)** 으로 간주됨.
프로그램의 복잡성이 증가할 수록 공유된 가변 상태에 대해 추론이 어렵기 떄문.
=> 가변 상태는 클래스 안에 가두는 것이 가장 좋다.

프로퍼티를 val로 선언하면 재대입 불가능해짐.
무조건 재대입만 가능하다.
```kotlin
class Sofa{
    val cover: String = "Loveseat cover"
}

fun main(){
    var sofa = Sofa()
    // sofa.cover = "New cover"
    // 허용되지 않음.
    sofa = Sofa()
    // var은 내부 프로퍼티 한정으로 접근 불가.
    // 재대입만 허용
}
```
<br/>
<br/>

--- 
# Atom19
### 생성자
생성자 파라미터를 var나 val로 지정하면 해당 식별자가 프로퍼티로 바뀌며, 생성자 밖에서도 이 식별자에 접근 가능.
val로 정의한 생성자 파라미터는 변경 불가.
var는 변경 가능.

```kotlin
class MutableNameAlien(var name: String)

class FixedNameAlien(val name: String)

fun main(){
    val alien1 = MutableNameAlien("Reverse Giraffe")
    val alien2 = FixedNameAlien("Krombopolis Michael")

    alien1.name = "Parasite"
    // 이건 불가능
    // alien2.name = "Parasite"
}
```

<br/>
<br/>

--- 
# Atom20
### 가시성 제한하기
**작성한 코드를 며칠 또는 몇 주 동안 보지 않다가 다시  살펴보면 그 코드를 작성하는 데 더 좋은 방법이 보일 수도 있다.**
이게 리팩터링을 하는 주된 이유다.
리팩터링은 코드를 고쳐 써서 더 읽기 좋고 이해하기 쉽게, 그래서 더 유지 보수하기 쉽게 만드는 것이다.
우리는 우리가 수정한 코드가 클라이언트 코드에 영향을 끼치지 않는다는 확신을 바탕으로 코드를 수정하고 개선해야한다.
<br/>
따라서 소프트웨어 설계 시, 일차적으로 고려할 내용은 다음과 같다.
**변화해야 하는 요소와 동일하게 유지되어야 하는 요소를 분리하라.**
<br/>
가시성 제어를 위한게 접근 변경자(access modifier)
-> public, private, protected, internal 등

<br/>
내부 구현을 노출시켜야 하는 경우를 제외하고는 프로퍼티나 멤버를 앵간해선 private으로 만드는 것이 안전하다.

<br/>
한 객체에 대해 참조를 여러 개 유지하는 경우를 에일리어싱(aliasing)이라고 하며, 이로 인해 놀랄 만한 동작(?)을 수행 가능.

```kotlin
class Counter(var start: Int){
    fun increment(){
        start +=1
    }
    override fun toString() = start.toString()
}

class CounterHolder(private val counter: Counter){
    override fun toString() = "CounterHolder: $counter"
}

fun main(){
    val c = Counter(11)
    val ch = CounterHolder(c)
    println(ch)
    c.increment()
    println(ch)
    val ch2 = CounterHolder(Counter(9))
    println(ch2)
}
```
<br/>

**internal** 키워드는 멀티 모듈 환경을 위해 필요한 접근 변경자이다.
모듈(Module)은 논리적으로 독립적인 각 부분을 뜻하는데, 빌드 시스템(Gradle, Maven)에 따라 달라진다.
Internal 정의는 그 정의가 포함된 모듈 내부에서만 접근 가능.
특정 요소를 private로 정의하자니 너무 제약이 심한거 같고, public이라기엔 애매한 경우 internal을 사용하는 것이 권장됨. 

<br/>
<br/>

---
# Atom 21
### 패키지
kotlin.math.의 ```roundToInt()```는 Double의 소수점 이하를 그냥 버리는 ```toInt```와 다르게 double을 가장 가까운 Int로 반올림 해준다.
유용할 듯.

<br/>
<br/>

---
# Atom 22
### Test
코드의 올바름을 검증 할 때, println()을 사용하는 방식은 부실한 접근 방법.

- JUnit: 자바에서 가장 널리 쓰이는 테스트 프레임워크...코틀린에서도 유용하게 사용 가능
- 코테스트(Kotest): 코틀린 전용 테스트 프레임워크
- 스펙(Spek) 프레임워크: 명세 테스트(Specification test)라는 다른 형태의 테스트를 제공

<br/>

**TDD(Test Driven Development)를 해라.**
코드를 작성하기 전에 테스트를 먼저 작성해 실패시킨 후, 나중에 테스트를 통과하도록 코드를 작성하난 기법이다.
이런 방법은 테스트를 염두하면서 "이 결과를 어떻게 테스트하지?"라는 질문을 끊임없이 스스로에게 되뇌이게 된다.
질문을 던진고 나면, 함수를 만들 때, 테스트 외의 다른 이유가 없더라도 테스트를 위해 함수가 무언가를 반환하도록 한다.
 

 <br/>
 <br/>

---
 # Atom 23
 ### 예외 처리 잘해라
 적절한 표준 예외 사용은 유지보수에 도움이 된다.
 ex) IllegalArgumentException, IOException 등.

 예외를 던질 때는 ```throw```키워드 다음에 던질 예외 이름을 넣고 그 위에 필요한 인자들을 추가
 ```kotlin
 thow IllegalArgumentException("Parma can't be zero")
 ``` 


 <br/>
 <br/>
 
 ---
 # Atom 24
 ### List
 컨테이너(container) == 컬렉션(Collection)
 기본적인 컨테이너를 생각할 때 보통 List를 많이 사용한다.