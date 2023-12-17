# Chapter 3:  사용성

#Atom 30
### 확장함수

확장함수는 기존 클래스에 멤버 함수를 추가하는 것과 같은 효과를 낸다.

확장할 대상 타입은 수신 객체 타입(receiver type)이라 함

확장 함수를 저으이하기 위해서는 함수 명 앞에 수신 객체 타입을 붙임

```fun 수신타입.확장함수() {...} ```

```kotlin
fun String.singleQuoted() = "`$this`"
fun String.doubleQuote() = "\"$this\""
```

이렇게

확장함수를 수신 객체 타입의 멤버 함수처럼 호출 가능

this 키워드를 사용해 멤버 함수나 다른 확장에 접근 가능.

클래스 내부에서 this를 생략 가능 하듯, 확장 함수 안에서도 this 생략 가능

명시적으로 멤버를 한정시킬 필요x

```kotlin
class Book(val title: String)

fun Book.categoize(category: String) =
    """title: "$title", category: "$category" """

fun main() {
    Book("Dracula").categoize("Vampire") eq
            """title: "Dracula", category: "Vampire"""
}
```

여기서 `categorize()`는 안에서 아무것도 한정 안해도 Book의 프로퍼티인 title에 접근 가능


확장 함수는 확장 대상 타입(수신 객체 타입)의 public 원소에만 접근 가능

따라서 확장 함수는 일반 함수가 할 수 있는 일만 처리 할 수 있다.

그럼 왜 씀??

=> 확장 함수는 오로지 this를 사용함으로써(또는 생략) **구문적 편의를 얻기 때문에 사용**한다.

<br/>
<br/>

---
# Atom 31
### 이름이 붙은 인자와 디폴트 인자

이름 붙은 인자는 가독성에서 좋다.

인자 목록이 긴 경우 특히 그럼.

다음 코드 처럼
```kotlin
fun color(red: Int, green: Int, blue: Int) =
    "($red, $green, $blue)"


fun main() {
    color(1, 2, 3) eq "(1, 2, 3)" // [1]
    color(                        // [2]
        red = 76,
        green = 89,
        blue = 0
    ) eq "(76, 89, 0)"
    color(52, 34, blue = 0) eq "(52, 34, 0)" // [3]
}
```

[1] 코드는 많은 정보를 전달 못한다. 이건 보는 사람이 직접 함수 문서를 살펴 봐야한다.
[2]는 모든 인자의 의미가 명확하다
[3]은 모든 인자에 이름을 붙이지 않아도 된다.

이름이 붙은 인자를 사용하면 초기화 순서를 변경할 수 있다.
아래처럼
```kotlin
fun main() {
    color(blue = 0, red = 99, green = 52) eq "(99, 52, 0)"
    color(red = 255, 255, 0) eq "(255, 255, 0)"
}
```

물론 순서를 변경해서 초기화하면 나머지 부분도 이름을 붙여줘야함.

컴파일러가 이름이 생략된 인자들의 위치를 알아내지 못할 수도 있기 때문이기도 하고, 읽는 사람 입장도 고려한 것.


이름 붙은 인자는 디폴트 인자(Default argument)와 결합하면 더 좋다.

디폴트 인자는 파라미터의 디폴드 값을 함수 정의에서 지정해 놓는거

```kotlin
fun color2(
    red: Int = 0,
    green: Int = 0,
    blue: Int = 0,
) =
    "($red, $green, $blue)"

fun main() {
    color2(139) eq "(139, 0, 0)"
    color2(blue = 139) eq "(0, 0, 139)"
    color2(255, 165) eq "(255, 165, 0)"
}
```

함수 호출 시 값을 지정하지 않는 인자는 자동으로 디폴트 값으로 지정된다.

위 코드에서 color2의 마지막 파라미터인 blue뒤에 덧붙임 콤마(trailing comma)를 사용한다(이거 뭔지 늘 궁금했음)
덧붙임 콤마는 마지막 파라미터(blue)뒤에 콤마를 추가로 붙인 건데, 파라미터 값을 여러 줄에 걸쳐 쓰는 경우 콤마를 추가하거나 빼지 않아도 새로운 아이템을 추가하거나 아이템 순서를 바꿀 수 있다..
이거 뿐이었군


생성자에 써도 됨.

`joinToString()`은 디폴트 인자를 사용하는 표준 라이브러리 함수다.

이터레이션이 가능한 객체(리스트, 집합, 범위 등)의 내용을 String으로 합쳐준다.
이때 원소 사이에 들어갈 문자열(구분자), 맨 앞에 붙일 문자열(접두사), 맨 뒤에 붙일 문자열(접미사)을 지정할 수도 있음. 


<br/>
<br/>

---
# Atom 32
## 오버로딩(Overload)

Overload는 "짐을 너무 많이 싣거나 무언가를 너무 많이 준다"는 뜻의  동사다.

함수의 이름을 그 대상으로 한다.

**파라미터 목록이 다른 함수에 같은 이름을 사용하는 것이 오버로딩**


```kotlin
class Overloading {
    fun f() = 0
    fun f(n: Int) = n + 2
}

fun main() {
    val o = Overloading()
    o.f() eq 0
    o.f(11) eq 13
}
```

함수의 **시그니처(signature)**는 함수 이름, 파라미터 목록, 반환 타입으로 이루어짐

코틀린은 시그니처를 비교해서 함수와 함수를 구분하는데, 함수를 오버로딩할 때는 함수 파라미터 리스트를 서로 다르게 만들어야 한다.

함수의 반환 타입은 오버로팅 대상이 아니다.


만약 어떤 클래스 안에 확장 함수와 시그니처가 똑같은 멤버 함수가 들어 있으면, 코틀린은 멤버 함수를 우선시 함.

하지만 확장 함수를 갖고 멤버 함수를 오버로딩할 수 있음.


```kotlin
class My {
    fun foo() = 0
}

fun My.foo() = 1 // 무의미

fun My.foo(i: Int) = i + 2

fun main() {
    My().foo() eq 0

    My().foo(1) eq 3
}
```

멤버와 시그니처가 중복되는 확장 함수를 호출해도 의미가 없다. 이런 확장 함수는 절대로 호출 안됨

무조건 다른 파라미터 목록을 제공해야 함.


<br/>

그리고 이런거 하지마라
```kotlin
fun f(n: Int) = n + 373
fun f() = f(0)

fun main() {
    f() eq 373
}
```
디폴트 인자를 흉내내기 위해 이렇게 작성하는 것은 의미도 없고 스파게티 마냥 코드만 꼬여서 가독성을 망친다.
파라미터가 없는 함수는 첫 번째 함수만 호출된다.

<br/>
그냥 디폴트 인자를 사용해라.

```kotlin
fun f2(n: Int = 0) = n + 373

fun main() {
    f2() eq 373
}
```

<br/>

함수 오버로딩과 디폴트 인자를 함께 사용하는 경우, 오버로딩한 함수를 호출하면 함수 시그니처와 함수 호출이 가장 가깝게 일치되는 함수를 호출함.

```Kotlin
fun foo(n: Int = 99) = trace("foo-1-$n")

fun foo() { // 이게 호출됨.
    trace("foo-2")
    foo(14)
}

fun main() {
    foo()
    trace eq """
        foo-2
        foo-1-14
    """
}
```


오버로딩이 왜 유용할까?

오버로딩을 사용하면 같은 주제를 다르게 변경한다는 개념을 더 명확히 표현 가능

```kotlin
fun addInt(i: Int, j: Int) = i + j
fun addDouble(i: Double, j: Double) = i + j


fun add(i: Int, j: Int) = i + j
fun add(i: Double, j: Double) = i + j

fun main() {
    addInt(5, 6) eq add(5, 6)
    addDouble(5.0, 6.0) eq add(5.0, 6.0)
}
```


<br/>
<br/>

---
# Atom 33
## When

when 식을 문처럼 취급하는 경우(즉, when의 결과를 사용하지 않는 경우) else 생략 가능

<br/>
<br/>

---
# Atom 34
## enum


enum class는 이름을 관리하는 편리한 방법이다.

```kotlin
enum class Level {
    Overflow, High, Medium, Low, Empty
}

fun main() {
    Level.Medium eq "Medium"
}
```

enum을 만들면 enum의 이름에 해당하는 문자열을 돌려주는 toString()이 자동으로 생성된다.

main()에서 Level.Medium을 사용한 것처럼 이넘 이름을 사용할 때는 반드시 이름을 한정시켜야 한다.

import로 이넘에 정의된 모든 이름을 현재의 nameSpace로 불러오면 한정 짓지 않아도 됨.

values()를 사용해 이넘 값을 이터레이션 할 수 있음.

values()는 Array를 반환

``` kotlin
enum.values()
```

<br/>
이넘은 인스턴스 개수가 미리 정해져 있고 클새ㅡ 본문 안에 이 모든 인스턴스가 나열되어 있는 특별한 종류의 클래스다.

이 점을 제외하면 enum은 일반 클래스와 똑같이 동작

따라서 멤버 함수나 멤버 프로퍼티를 이넘에 정의 할 수도 있다.




