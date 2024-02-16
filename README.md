# 스프링 MVC 1,2편 토이프로젝트 내용 정리
> 김영한 강사님 MVC 1편 내용의 웹페이지 만들기, 2편의 스프링 통합과폼,메시지국제화, 검증, 로그인처리 까지의 내용을 포함하고 있습니다.

***
# 1. 요구사항 분석

* 상품 도메인 모델
  + 상품 ID
  + 상품명
  + 가격
  + 수량
* 상품 관리 기능
  + 상품 목록
  + 상품 상세
  + 상품 등록
  + 상품 수정

****
# 2. 서비스 제공 흐름
![image](https://github.com/helloJosh/spring-item-service-study/assets/37134368/398c219c-b74a-4832-8c07-c48d5b2b8373)

***
# 3. 상품 도메인 개발
##### 상품 객체
```java
@Data
public class Item {
  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  public Item() {
  }
  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }
}
```
* Item - 상품 객체
* Null 값을 사용할 수도 있기 때문에 Integer형을 사용한다.

##### 상품 저장소
```java
@Repository
public class ItemRepository {
  private static final Map<Long, Item> store = new HashMap<>(); //static 사용
  private static long sequence = 0L; //static 사용

  public Item save(Item item) {
    item.setId(++sequence);
    store.put(item.getId(), item);
    return item;
  }
  public Item findById(Long id) {
    return store.get(id);
  }
  public List<Item> findAll() {
    return new ArrayList<>(store.values());
  }
  public void update(Long itemId, Item updateParam) {
    Item findItem = findById(itemId);
    findItem.setItemName(updateParam.getItemName());
    findItem.setPrice(updateParam.getPrice());
    findItem.setQuantity(updateParam.getQuantity());
  }
  public void clearStore() {
    store.clear();
  }
}
```
* ItemRepository - 상품 저장소

***
# 4. 상품 서비스 HTML
##### 상품 목록 HTML
```html
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
  </head>
  <body>
    <div class="container" style="max-width: 600px">
      <div class="py-5 text-center">
        <h2>상품 목록</h2>
      </div>
      <div class="row">
        <div class="col">
          <button class="btn btn-primary float-end" onclick="location.href='addForm.html'" type="button">
            상품 등록
          </button>
        </div>
      </div>
    <hr class="my-4">
    <div>
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>수량</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><a href="item.html">1</a></td>
            <td><a href="item.html">테스트 상품1</a></td>
            <td>10000</td>
            <td>10</td>
          </tr>
          <tr>
            <td><a href="item.html">2</a></td>
            <td><a href="item.html">테스트 상품2</a></td>
            <td>20000</td>
            <td>20</td>
          </tr>
        </tbody>
      </table>
      </div>
    </div> <!-- /container -->
  </body>
</html>
```
##### 상품상세 HTML
```html
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
    .container {
    max-width: 560px;
    }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="py-5 text-center">
        <h2>상품 상세</h2>
      </div>
      <div>
        <label for="itemId">상품 ID</label>
        <input type="text" id="itemId" name="itemId" class="form-control" value="1" readonly>
      </div>
      <div>
        <label for="itemName">상품명</label>
        <input type="text" id="itemName" name="itemName" class="form-control" value="상품A" readonly>
      </div>
      <div>
        <label for="price">가격</label>
        <input type="text" id="price" name="price" class="form-control" value="10000" readonly>
      </div>
      <div>
        <label for="quantity">수량</label>
        <input type="text" id="quantity" name="quantity" class="form-control" value="10" readonly>
      </div>

      <hr class="my-4">

      <div class="row">
        <div class="col">
          <button class="w-100 btn btn-primary btn-lg" onclick="location.href='editForm.html'" type="button">상품 수정</button>
        </div>
        <div class="col">
          <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'" type="button">목록으로</button>
        </div>
      </div>
    </div> <!-- /container -->
  </body>
</html>
```

##### 상품 등록 폼 HTML
```html
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
    .container {
    max-width: 560px;
    }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="py-5 text-center">
        <h2>상품 등록 폼</h2>
      </div>

      <h4 class="mb-3">상품 입력</h4>
      <form action="item.html" method="post">
        <div>
        <label for="itemName">상품명</label>
        <input type="text" id="itemName" name="itemName" class="formcontrol" placeholder="이름을 입력하세요">
        </div>

        <div>
        <label for="price">가격</label>
        <input type="text" id="price" name="price" class="form-control" placeholder="가격을 입력하세요">
        </div>

        <div>
        <label for="quantity">수량</label>
        <input type="text" id="quantity" name="quantity" class="formcontrol" placeholder="수량을 입력하세요">
        </div>

        <hr class="my-4">

        <div class="row">
          <div class="col">
            <button class="w-100 btn btn-primary btn-lg" type="submit">상품 등록</button>
          </div>
          <div class="col">
            <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'" type="button">취소</button>
          </div>
        </div>
      </form>
    </div> <!-- /container -->
  </body>
</html>
```
##### 상품 수정 폼 HTML
``` html
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
    .container {
    max-width: 560px;
    }
    </style>
  </head>
  <body>
  <div class="container">
    <div class="py-5 text-center">
      <h2>상품 수정 폼</h2>
    </div>

    <form action="item.html" method="post">
      <div>
        <label for="id">상품 ID</label>
        <input type="text" id="id" name="id" class="form-control" value="1" readonly>
      </div>

      <div>
        <label for="itemName">상품명</label>
        <input type="text" id="itemName" name="itemName" class="formcontrol" value="상품A">
      </div>

      <div>
        <label for="price">가격</label>
        <input type="text" id="price" name="price" class="form-control" value="10000">
      </div>

      <div>
        <label for="quantity">수량</label>
        <input type="text" id="quantity" name="quantity" class="formcontrol" value="10">
      </div>

      <hr class="my-4">
      <div class="row">
        <div class="col">
          <button class="w-100 btn btn-primary btn-lg" type="submit">저장</button>
        </div>
        <div class="col">
          <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='item.html'" type="button">취소</button>
        </div>
      </div>
    </form>
  </div> <!-- /container -->
  </body>
</html>
```
****
# 5. 상품 서비스 HTML을 타임리프로 변환
##### 상품 목록 
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
  </head>
  <body>
    <div class="container" style="max-width: 600px">
      <div class="py-5 text-center">
        <h2>상품 목록</h2>
      </div>

      <div class="row">
        <div class="col">
          <button class="btn btn-primary float-end" onclick="location.href='addForm.html'" th:onclick="|location.href='@{/basic/items/add}'|" type="button">상품 등록</button>
        </div>
      </div>

      <hr class="my-4">
      <div>
        <table class="table">
          <thead>
          <tr>
          <th>ID</th>
          <th>상품명</th>
          <th>가격</th>
          <th>수량</th>
          </tr>
          </thead>
        <tbody>
          <tr th:each="item : ${items}">
          <td><a href="item.html" th:href="@{/basic/items/{itemId} (itemId=${item.id})}" th:text="${item.id}">회원id</a></td>
          <td><a href="item.html" th:href="@{|/basic/items/${item.id}|}" th:text="${item.itemName}">상품명</a></td>
          <td th:text="${item.price}">10000</td>
          <td th:text="${item.quantity}">10</td>
          </tr>
        </tbody>
        </table>
      </div>
    </div> <!-- /container -->
  </body>
</html>
```
* 타임리프 사용 선언 : `<html xmlns:th="http://www.thymeleaf.org">`
* 속성 변경 : `th:href="@{/css/bootstrap.min.css}"`
  + `href="value1"` 을 `th:href="value2"` 의 값으로 변경한다.
  + 타임리프 뷰 템플릿을 거치게 되면 원래 값을 `th:xxx` 값으로 변경한다. 만약 값이 없다면 새로 생성
  + HTML을 그대로 볼때는 `href` 속성이 사용되고, 뷰 템플릿을 거치면 `th:href`의 값이 href로 대체되면서 동적으로 변경된다.
  + 대부분의 thml 속성을 `th:xxx`로 변경할 수 있다.

<br/>

* 타임리프의 핵심
  + 핵심은 `th:xxx`가 붙은 부분은 서버사이드에서 렌더링 되고, 기존 것을 대체한다. `th:xxx`이 없으면 기존 html의 `xxx` 속성을 그대로 사용한다.
  + HTML을 파일로 직접 열었을 때, `th:xxx`가 있어도 웹 브라우저는 `th:` 속성을 알지 못하므로 무시한다.
  + 따라서 HTML을 파일 보기를 유지하면서 템플릿 기능도 할 수 있다.
 

<br/>

* URL 링크 표현식 - `th:href="@{/css/bootstrap.min.css}"`
  + `@{...}` : 타임리프는 URL 링크를 사용하는 경우 `@{...}` 를 사용한다. 이것을 URL 링크 표현식이라 한다.
  + URL 링크 표현식을 사용하면 서블릿 컨텍스트를 자동으로 포함한다.

<br/>


* 상품 등록 폼으로 이동 속성 변경 -`th:onclick`
  + `onclick="location.href='addForm.html'"`
  + `th:onclick="|location.href='@{/basic/items/add}'|"`

<br/>

* 리터럴 대체 - `|...|`
  + 타임 리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야한다.
    * `<span th:text="'Welcome to our application, '+${user.name}+'!'">`
   
  + 다음과 같이 리터럴 대체 문법을 사용하면, 더하기 없이 편리하게 사용할 수 있다.
    * `<span th:text="|Welcome to our application, ${user.name}!|">`
   
  + 결과를 다음과 같이 만들어야 하는데
    * `location.href='/basic/items/add'`
   
  + 그냥 사용하면 문자와 표현식을 각각 따로 더해서 사용해야하므로 다음과 같이 복잡해진다.
    * `th:onclick="'loation.href='+'\''+@{/basic/items/add}+'\''"`
   
  + 리터럴 대체 문법을 사용하면 다음과 같이 편리하게 사용할 수 있다.
    * `th:onclick="|location.href='@{/basic/items/add}'|"`

<br/>   

* 반복 출력 - `th:each`
  + `<tr th:each="item:${items}">`
  + 반복은 `th:each`를 사용한다. 이렇게하면 모델에 포한됨 `items` 컬렉션 데이터가 `item`변수에 하나씩 포함되고, 반복문 안에 `item`변수를 사용할 수 있다.
  + 컬렉션 수 만큼 `<tr>..</tr>`이 하위 태그를 포함해서 생성된다.

<br/>   

* 변수 표현식 - `${..}`
  + `<td th:text="${item.price}">10000</td>`
  + 모델에 포함된 값이나 타임리프 변수로 선언한 값을 조회할 수 있다.
  + 프로퍼티 접근법을 사용한다(`item.getPrice()`)

<br/>

* 내용 변경 - `th:text`
  + `<td  th:text="${item.price}">10000</td>`
  + 내용의 값을 `th:text` 의 값으로 변경한다.
  + 여기서는 10000을 `${item.price}`의 값으로 변경한다.

<br/>

* URL 링크표현식2 -`@{..}`
  + `th:href="@{/basic/items/{itemId}(itemId=${item.id})}"`
  + 상품 ID를 선택하는 링크를 확인해보자
  + URL 링크 포현식을 사용하면 경로를 템플릿처럼 편리하게 사용할 수 있다.
  + 경로변수({itemId}) 뿐만 아니라 쿼리 파라미터도 생성한다.(타임리프 기본편 참고)
  + 예) `th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}"`
    * 생성링크 : `http://localhost:8080/basic/items/1?query=test`

<br/>  

* URL 링크 간단히
  + `th:href="@{|/basic/items/${item.id}|}"`
  + 상품 이름을 선택하는 링크를 확인해보자.
  + 리터럴 대체 문법을 활용해서 간단히 사용할 수도 있다.

##### 상품상세
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
  <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
    <style>
    .container {max-width: 560px;}
    </style>
  </head>
  <body>
    <div class="container">
      <div class="py-5 text-center">
      <h2>상품 상세</h2>
      </div>

      <div>
        <label for="itemId">상품 ID</label>
        <input type="text" id="itemId" name="itemId" class="form-control" value="1" th:value="${item.id}" readonly>
      </div>
      <div>
        <label for="itemName">상품명</label>
        <input type="text" id="itemName" name="itemName" class="form-control" value="상품A" th:value="${item.itemName}" readonly>
      </div>

      <div>
        <label for="price">가격</label>
        <input type="text" id="price" name="price" class="form-control" value="10000" th:value="${item.price}" readonly>
      </div>
      <div>
        <label for="quantity">수량</label>
        <input type="text" id="quantity" name="quantity" class="form-control" value="10" th:value="${item.quantity}" readonly>
      </div>

      <hr class="my-4">
      <div class="row">
        <div class="col">
          <button class="w-100 btn btn-primary btn-lg" onclick="location.href='editForm.html'" th:onclick="|location.href='@{/basic/items/{itemId}/ edit(itemId=${item.id})}'|" type="button">상품 수정</button>
        </div>
        <div class="col">
          <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'" th:onclick="|location.href='@{/basic/items}'|" type="button">목록으로</button>
        </div>
      </div>
    </div> <!-- /container -->
  </body>
</html>
```
* 속성 변경 - `th:value="${item.id}`
  + 모델에 있는 item 정보를 획득하고 프로퍼티 접근법으로 출력한다.(`item.getId()`)
  + `value` 속성을 `th:value` 속성으로 변경한다.

<br/>

* 상품수정 링
  + `th:onclick="|location.href='@{/basic/items/{itemId}/edit(itemId=${item.id})}'|"`

<br/>

* 목록으로 링크
  + `th:onclick="|location.href='@{/basic/items}'|"`

<br/>  

##### 상품 등록 폼 

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
    <style>
    .container {max-width: 560px;}
    </style>
  </head>
  <body>
    <div class="container">
      <div class="py-5 text-center">
        <h2>상품 등록 폼</h2>
      </div>

      <h4 class="mb-3">상품 입력</h4>
      <form action="item.html" th:action method="post">
        <div>
          <label for="itemName">상품명</label>
          <input type="text" id="itemName" name="itemName" class="formcontrol" placeholder="이름을 입력하세요">
        </div>
        <div>
          <label for="price">가격</label>
          <input type="text" id="price" name="price" class="form-control" placeholder="가격을 입력하세요">
        </div>
        <div>
          <label for="quantity">수량</label>
          <input type="text" id="quantity" name="quantity" class="formcontrol" placeholder="수량을 입력하세요">
        </div>

        <hr class="my-4">
        <div class="row">
          <div class="col">
            <button class="w-100 btn btn-primary btn-lg" type="submit">상품 등 록</button>
          </div>
          <div class="col">
            <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'" th:onclick="|location.href='@{/basic/items}'|" type="button">취소</button>
          </div>
        </div>
      </form>
    </div> <!-- /container -->
  </body>
</html>
```
* 속성 변경 - `th:action`
  + HTML form에서 `action`에 값이 없으면 현재 URL에 데이터를 전송한다.
  + 상품 등록 폼의 URL과 실제 상품 등록을 처리하는 URL을 똑같이 맞추고 HTTP 메서드로 두 기능을 구분한다.
    * 상품 등록 폼 : GET `/basic/item/add`
    * 상품 등록 처리: POST `/basic/item/add`
  + 이렇게 하면 하나의 URL로 등록폼과 등록 처리를 깔끔하게 처리할 수 있다.

<br/>

* 취소
  + 취소시 상품 목록을 이동한다
  + `th:onclick="|location.href='@{/basic/items}'|"`

<br/>
***
# 6. 상품 등록 처리 - @ModelAttribute
### 6.1. addItemV2 - 상품 등록 처리 - ModelAttribute
```java
/**
* @ModelAttribute("item") Item item
* model.addAttribute("item", item); 자동 추가
*/
@PostMapping("/add")
public String addItemV2(@ModelAttribute("item") Item item, Model model) {
  itemRepository.save(item);
  //model.addAttribute("item", item); //자동 추가, 생략 가능
  return "basic/item";
}
```
* **@ModelAttribute - 요청 파라미터 처리**
  + `@ModelAttribute` 는 `Item` 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
 
* **@ModelAttribute - Model 추가**
  + `@ModelAttribute` 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 `@ModelAttribute` 로 지정한 객체를 자동으로 넣어준다. 지금 코드를 보면 `model.addAttribute("item", item)` 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.
 
* 모델에 데이터를 담을 때는 이름이 필요하다. 이름은 `@ModelAttribute` 에 지정한 `name(value)` 속성을 사용한다. 만약 다음과 같이 `@ModelAttribute` 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.
  + `@ModelAttribute("hello") Item item` ➡️ 이름을 `hello` 로 지정
  + `model.addAttribute("hello", item);` ➡️ 모델에 `hello` 이름으로 저장
 
### 6.2. addItemV3 - ModelAttribute 이름 생략
```java
/**
* @ModelAttribute name 생략 가능
* model.addAttribute(item); 자동 추가, 생략 가능
* 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
*/
@PostMapping("/add")
public String addItemV3(@ModelAttribute Item item) {
  itemRepository.save(item);
  return "basic/item";
}
```
* `@ModelAttribute`의 이름을 생략할 수 있다.
* `@ModelAttribute`의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 등록된다.
  + 예)`@ModelAttribute` 클래스명 모델에 자동 추가되는 이름
    * `Item` ➡️ `item`
   

### 6.3. addItemV4 - 상품등록처리 - ModelAttribute 전체 생략
```java
/**
* @ModelAttribute 자체 생략 가능
* model.addAttribute(item) 자동 추가
*/
@PostMapping("/add")
public String addItemV4(Item item) {
  itemRepository.save(item);
  return "basic/item";
}
```
* `@ModelAttribute` 자체도 생략 가능

***
# 7. 상품 수정
### 7.1. 상품수정폼 컨트롤러
##### BasicItemController
```java
@GetMapping("/{itemId}/edit")
public String editForm(@PathVariable Long itemId, Model model) {
  Item item = itemRepository.findById(itemId);
  model.addAttribute("item", item);
  return "basic/editForm";
}
```

##### 상품 수정 폼 뷰
``` html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <link href="../css/bootstrap.min.css" th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
    <style>
    .container {max-width: 560px;}
    </style>
  </head>
  <body>
    <div class="container">
      <div class="py-5 text-center">
        <h2>상품 수정 폼</h2>
      </div>

      <form action="item.html" th:action method="post">
        <div>
          <label for="id">상품 ID</label>
          <input type="text" id="id" name="id" class="form-control" value="1" th:value="${item.id}" readonly>
        </div>
        <div>
          <label for="itemName">상품명</label>
          <input type="text" id="itemName" name="itemName" class="formcontrol" value="상품A" th:value="${item.itemName}">
        </div>
        <div>
          <label for="price">가격</label>
          <input type="text" id="price" name="price" class="form-control" th:value="${item.price}">
        </div>
        <div>
          <label for="quantity">수량</label>
          <input type="text" id="quantity" name="quantity" class="formcontrol" th:value="${item.quantity}">
        </div>

        <hr class="my-4">
        <div class="row">
          <div class="col">
            <button class="w-100 btn btn-primary btn-lg" type="submit">저장</button>
          </div>
          <div class="col">
            <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='item.html'" th:onclick="|location.href='@{/basic/items/{itemId}(itemId=${item.id})}'|" type="button">취소</button>
          </div>
        </div>
      </form>
    </div> <!-- /container -->
  </body>
</html>
```
* 이전에 작성한 파일과 똑같다

##### 상품 수정 개발
``` java
@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
  itemRepository.update(itemId, item);
  return "redirect:/basic/items/{itemId}";
}
```
* 상품 수정은 상품 등록과 전체 프로세스가 유사하다.
* GET `/items/{itemId}/edit` : 상품 수정 폼
* POST `/items/{itemId}/edit` : 상품 수정 처리

##### 리다이렉트
* 상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트 호출한다.
  + 스프링은 `redirect:/...`으로 편리하게 리다이렉트를 지원한다.
* `redirect:/basic/items/{itemId}`
  + 컨트롤러에 매핑된 `@PathVariable` 의 값은 `redirect` 에도 사용 할 수 있다.
  + `redirect:/basic/items/{itemId}` `{itemId}` 는 `@PathVariable Long itemId` 의 값을 그대로 사용한다.
 
***
# 8. PRG Post/Redirect/Get
### 8.1. 문제점
![image](https://github.com/helloJosh/springmvc-itemservice-toy/assets/37134368/0608d029-66fa-4956-b0f2-6c062188d87b)
![image](https://github.com/helloJosh/springmvc-itemservice-toy/assets/37134368/c2f7d7fe-4ec5-409b-b446-ef9872780423)
* 그림과 같이 POST 등록 후 새로고침을 하게 되면 아래와 같은 방식으로 중복으로 데이터가 추가된다
  + 상품 등폭 폼에서 데이터를 입력하고 저장을 선택하면 `POST /add` + 상품 데이터를 서버로 전송한다.
  + 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 `POST /add` + 상품 데이터를 서버에 다시 전송하게 된다.
  + 따라서 내용은 같고, ID만 다른 상품 데이터가 쌓인다.

### 8.2. 해결법
##### POST, Redirect GET
![image](https://github.com/helloJosh/springmvc-itemservice-toy/assets/37134368/bab4343b-33b9-43f1-b42e-9741e9b717ff)
* 상품 저장 후에 뷰 템플릿 으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해준다.
* 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다.
* 마지막에 호출한 내용이 상품 상세 화면인 `GET /items/{id}` 가 된다.
* 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.

```java
/**
* PRG - Post/Redirect/Get
*/
@PostMapping("/add")
public String addItemV5(Item item) {
  itemRepository.save(item);
  return "redirect:/basic/items/" + item.getId();
}
```
* 이러한 문제 해결 방식을 `PRG Post/Redirect/Get` 라 한다.
* 하지만 이러한 코드는 URL에 변수를 더하는 것이기 때문에 URL 인코딩이 안될 경우가 있어 위험하다.

##### RedirectAttributes
``` java
/**
* RedirectAttributes
*/
@PostMapping("/add")
public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
  Item savedItem = itemRepository.save(item);
  redirectAttributes.addAttribute("itemId", savedItem.getId());
  redirectAttributes.addAttribute("status", true);
  return "redirect:/basic/items/{itemId}";
}
```
* `RedirectAttributes`를 사용하면 URL 인코딩도 해주고, `pathVariable`, 쿼리 파라미터까지 처리해준다
  + `redirect:/basic/items/{itemId}`
    * pathVariable 바인딩: `{itemId}`
    * 나머지는 쿼리 파라미터로 처리: `?status=true`
   
##### 뷰 템플릿 메시지 추가
```html
<div class="container">
  <div class="py-5 text-center">
    <h2>상품 상세</h2>
  </div>

  <!-- 추가 -->
  <h2 th:if="${param.status}" th:text="'저장 완료!'"></h2>
```
* `th:if` : 해당 조건이 참이면 실행
* `${param.status}` : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능
  + 원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다. 그런데 쿼리 파라미터는 자주 사용해서 타임리프에서 직접 지원한다.


***
# 9. 입력 폼 처리
* `th:object` : 커맨드 객체를 지정한다.
* `*{...}` : 선택 변수 식이라고 한다. `th:oject`에서 선택한 객체에 접근한다.
* `th:field` : HTML 태그의 `id`,`name`,`value` 속성을 자동으로 처리해준다.

##### 렌더링 전
`<input type="text" th:field="*{itemName}" />`
##### 렌더링 후
`<input type="text" id="itemName" name="itemName" th:value="*{itemName}" />`

##### 사용예시 - 등록 폼
```java
@GetMapping("/add")
public String addForm(Model model) {
  model.addAttribute("item", new Item());
  return "form/addForm";
}
```
* 새로운 아이템 객체 전달
```html
<form action="item.html" th:action th:object="${item}" method="post">
  <div>
    <label for="itemName">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}" class="formcontrol" placeholder="이름을 입력하세요">
  </div>
  <div>
    <label for="price">가격</label>
    <input type="text" id="price" th:field="*{price}" class="form-control" placeholder="가격을 입력하세요">
  </div>
  <div>
    <label for="quantity">수량</label>
    <input type="text" id="quantity" th:field="*{quantity}" class="formcontrol" placeholder="수량을 입력하세요">
  </div>
```
* `th:object="${item}"`: `<form>`에서 사용할 객체를 지정한다. 객체 지정시, 선택 변수식 `(*{...})`을 적용할 수 있다.
* `th:field="*{itemName}"`
    + `*{itemName}`는 선택 변수 식을 사용, `${item.itemName}`과 같다. 앞서 `th:object`로 `item`을 선택했기 때문에 선택 변수식을 사용할 수 있다.
    + `th:field`는 `id`, `name`, `value` 속성을 모두 자동으로 만들어 준다.
        * `id` : `th:field`에서 지정한 변수 이름과 같다.
        * `name` : `th:field`에서 지정한 변수 이름과 같다.
        * `value` : `th:field`에서 지정한 변수의 값을 사용한다.
     
##### 사용예시 - 수정폼
``` java
@GetMapping("/{itemId}/edit")
public String editForm(@PathVariable Long itemId, Model model) {
  Item item = itemRepository.findById(itemId);
  model.addAttribute("item", item);
  return "form/editForm";
}
```
* 찾은 아이템 객체 전달
``` html
<form action="item.html" th:action th:object="${item}" method="post">
  <div>
    <label for="id">상품 ID</label>
    <input type="text" id="id" th:field="*{id}" class="form-control" readonly>
  </div>
  <div>
    <label for="itemName">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}" class="formcontrol">
  </div>
  <div>
    <label for="price">가격</label>
    <input type="text" id="price" th:field="*{price}" class="form-control">
  </div>
  <div>
    <label for="quantity">수량</label>
    <input type="text" id="quantity" th:field="*{quantity}" class="formcontrol">
  </div>
```
* 수정폼은 `id`,`name`,`value` 모두 신경 써야했지만 `th:field` 덕분에 자동으로 처리
* 렌더링 전
  + `<input type="text" id="itemName" th:field="*{itemName}" class="form-control">`
* 렌더링 후
  + `<input type="text" id="itemName" class="form-control" name="itemName" value="itemA">`

***
# 10. 체크박스 - 단일1
##### 체크 박스 단일 예시
```html
<!-- single checkbox -->
<div>판매 여부</div>
<div>
  <div class="form-check">
    <input type="checkbox" id="open" name="open" class="form-check-input">
    <label for="open" class="form-check-label">판매 오픈</label>
  </div>
</div>
```
```java
@Slf4j
@PostMapping("/add")
public String addItem(Item item, RedirectAttributes redirectAttributes) {
  log.info("item.open={}", item.getOpen());
  ...중략...
}
```
##### 실행 로그
```java
FormItemController : item.open=true //체크 박스를 선택하는 경우
FormItemController : item.open=null //체크 박스를 선택하지 않는 경우
```
* form에서 `open=on`이라는 값을 넘기는데. 스프링은 on이라는 문자를 true 타입으로 변환해서 가져오는 것을 알 수 있다.
> 주의! html에서 체크 박스를 선택하지 않고 폼을 전송하면 `open`이라는 필드 자체가 서버로 전송 되지 않는다.

##### 체크 해제 인식 히든 필드
`<input type="hidden" name="_open" value="on"/>`
```html
<!-- single checkbox -->
<div>판매 여부</div>
<div>
  <div class="form-check">
    <input type="checkbox" id="open" name="open" class="form-check-input">
    <input type="hidden" name="_open" value="on"/> <!-- 히든 필드 추가 -->
    <label for="open" class="form-check-label">판매 오픈</label>
  </div>
</div>
```
* html checkbox는 선택이 안되면 클라이언트에서 서버로 값 자체를 보내지 않기 때문에 사용자가 의도적으로 체크되어 있던 값을 체크 해제해도 저장시 아무값도 넘어가지 않는다.
* 이런 문제를 해결하기 위해 스프링 MVC는 히든 필드를 만들어서 `_open`처럼 기준 체크 박스 이름 앞에 언더 스코터를 붙여 전송하면 체크를 해제했다고 인식할 수 있다.
* 체크 박스 체크
  + `open=on&_open=on`
  + 체크 박스를 체크하면 스프링 MVC가 `open` 에 값이 있는 것을 확인하고 사용한다. 이때 `_open` 은 무시한다.
* 체크 박스 미 체크
  + `_open=on`
  + 체크 박스를 체크하지 않으면 스프링 MVC가 `_open` 만 있는 것을 확인하고, `open` 의 값이 체크되지 않았다고 인식한다.
  + 이 경우 서버에서 `Boolean` 타입을 찍어보면 결과가 `null` 이 아니라 `false` 인 것을 확인할 수 있다. `log.info("item.open={}", item.getOpen());`
 
***
# 11. 체크박스 - 멀티
* 체크 박스 멀티를 사용하여 하나 이상을 체크 할 수 있다.
* 등록 지역
  + 서울, 부산, 제주
  + 체크 박스로 다중 선택할 수있는 기능
 
##### @ModelAttribute의 특별한 사용법
```java
@ModelAttribute("regions")
public Map<String, String> regions() {
  Map<String, String> regions = new LinkedHashMap<>();
  regions.put("SEOUL", "서울");
  regions.put("BUSAN", "부산");
  regions.put("JEJU", "제주");
  return regions;
}
```
* 등록폼, 상세화면, 수정폼에서 모두 서울, 부산, 제주라는 체크박스를 반복해서 보여줘야한다.
* 각각의 컨트롤러에서 `model.addAttribute(...)`을 사용해서 체크박스를 구성하는 데이터를 반복해서 넣어줘야한다.
* `@ModelAttribute` 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
* 이렇게하면 해당 컨트롤러를 요청할 때 `regions` 에서 반환한 값이 자동으로 모델( `model` )에 담기게 된다.
> 물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.

```html
<!-- multi checkbox -->
<div>
  <div>등록 지역</div>
  <div th:each="region : ${regions}" class="form-check form-check-inline">
    <input type="checkbox" th:field="*{regions}" th:value="${region.key}" class="form-check-input">
    <label th:for="${#ids.prev('regions')}" th:text="${region.value}" class="form-check-label">서울</label>
  </div>
</div>
```
* `th:for="${#ids.prev('regions')}"`
* 체크박스는 같은 이름의 여러 체크박스를 만들 수 있다. 그런데 문제는 이렇게 반복해서 HTML 태그를 생성할 때, 생성된 HTML 태그 속성에서 `name` 은 같아도 되지만, `id` 는 모두 달라야 한다. 따라서 타임리프는 체크박스를 `each` 루프 안에서 반복해서 만들 때 임의로 `1` , `2` , `3` 숫자를 뒤에 붙여준다.


##### 생성 결과
``` html
<!-- multi checkbox -->
<div>
<div>등록 지역</div>
  <div class="form-check form-check-inline">
    <input type="checkbox" value="SEOUL" class="form-check-input" id="regions1" name="regions">
    <input type="hidden" name="_regions" value="on"/>
    <label for="regions1" class="form-check-label">서울</label>
  </div>
  <div class="form-check form-check-inline">
    <input type="checkbox" value="BUSAN" class="form-check-input" id="regions2" name="regions">
    <input type="hidden" name="_regions" value="on"/>
    <label for="regions2" class="form-check-label">부산</label>
  </div>
  <div class="form-check form-check-inline">
    <input type="checkbox" value="JEJU" class="form-check-input" id="regions3" name="regions">
    <input type="hidden" name="_regions" value="on"/>
    <label for="regions3" class="form-check-label">제주</label>
  </div>
</div>
<!-- -->
```
* `<label for="id 값">` 에 지정된 `id` 가 `checkbox` 에서 동적으로 생성된 `regions1` , `regions2` , `regions3` 에 맞추어 순서대로 입력된 것을 확인할 수 있다.

##### item.html 추가
``` html
<!-- multi checkbox -->
<div>
  <div>등록 지역</div>
  <div th:each="region : ${regions}" class="form-check form-check-inline">
    <input type="checkbox" th:field="${item.regions}" th:value="${region.key}" class="form-check-input" disabled>
    <label th:for="${#ids.prev('regions')}" th:text="${region.value}" class="form-check-label">서울</label>
  </div>
</div>
```
##### editForm.html 추가
```html
<!-- multi checkbox -->
<div>
  <div>등록 지역</div>
  <div th:each="region : ${regions}" class="form-check form-check-inline">
    <input type="checkbox" th:field="${regions}" th:value="${region.key}" class="form-check-input">
    <label th:for="${#ids.prev('regions')}" th:text="${region.value}" class="form-check-label">서울</label>
  </div>
</div>
```

***
# 12. 라디오 버튼
``` java
@ModelAttribute("itemTypes")
public ItemType[] itemTypes() {
  return ItemType.values();
}
```
* ModelAttribute 사용

##### addForm.html
```html
<!-- radio button -->
<div>
  <div>상품 종류</div>
  <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
    <input type="radio" th:field="*{itemType}" th:value="${type.name()}" class="form-check-input">
    <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">
    BOOK
    </label>
  </div>
</div>
```
* 라디오 버튼은 무조건 하나를 선택하게 되어있기 때문에 히든 필드를 사용할 필요가 없다


```item.html
<!-- radio button -->
<div>
  <div>상품 종류</div>
  <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
    <input type="radio" th:field="${item.itemType}" th:value="${type.name()}" class="form-check-input" disabled>
    <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">
    BOOK
    </label>
  </div>
</div>
```
* `item.html` 에는 `th:object` 를 사용하지 않았기 때문에 `th:field` 부분에 `${item.itemType}` 으로 적어주어야 한다.

##### editform.html
```html
<!-- radio button -->
<div>
  <div>상품 종류</div>
  <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
    <input type="radio" th:field="*{itemType}" th:value="${type.name()}" class="form-check-input">
    <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">
    BOOK
    </label>
  </div>
</div>
```

##### 생성 결과
``` html
<!-- radio button -->
<div>
<div>상품 종류</div>
  <div class="form-check form-check-inline">
    <input type="radio" value="BOOK" class="form-check-input" id="itemType1" name="itemType">
    <label for="itemType1" class="form-check-label">도서</label>
  </div>

  <div class="form-check form-check-inline">
    <input type="radio" value="FOOD" class="form-check-input" id="itemType2" name="itemType" checked="checked">
    <label for="itemType2" class="form-check-label">식품</label>
  </div>

  <div class="form-check form-check-inline">
    <input type="radio" value="ETC" class="form-check-input" id="itemType3" name="itemType">
    <label for="itemType3" class="form-check-label">기타</label>
  </div>
</div>
```
##### 타임리프에서 ENUM 직접 사용하기
```java
@ModelAttribute("itemTypes")
public ItemType[] itemTypes() {
  return ItemType.values();
}
```
`<div th:each="type : ${T(hello.itemservice.domain.item.ItemType).values()}">`
* 위와 같이 직접 접근


***
# 13. 셀렉트 박스
##### FormItemController 추가
```java
@ModelAttribute("deliveryCodes")
public List<DeliveryCode> deliveryCodes() {
List<DeliveryCode> deliveryCodes = new ArrayList<>();
  deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
  deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
  deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
  return deliveryCodes;
}
```

##### addForm.html
```html
<!-- SELECT -->
<div>
  <div>배송 방식</div>
  <select th:field="*{deliveryCode}" class="form-select">
    <option value="">==배송 방식 선택==</option>
    <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}" th:text="${deliveryCode.displayName}">FAST</option>
  </select>
</div>
```

##### item.html
```html
<div>
<div>배송 방식</div>
  <select th:field="${item.deliveryCode}" class="form-select" disabled>
    <option value="">==배송 방식 선택==</option>
    <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}"th:text="${deliveryCode.displayName}">FAST</option>
  </select>
</div>
<hr class="my-4">
```
* `item.html`에서는 `th:object`를 사용하지 않기 때문에 `th:feild` 부분에 `${item.deliveryCode}`으로 적어주어야한다.
* `disabled`를 사용해서 상품 상세에서는 셀렉트 박스가 선택되지 않도록 했다.

##### editForm.html
```html
<!-- SELECT -->
<div>
<div>배송 방식</div>
  <select th:field="*{deliveryCode}" class="form-select">
    <option value="">==배송 방식 선택==</option>
    <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}"th:text="${deliveryCode.displayName}">FAST</option>
  </select>
</div>
<hr class="my-4">
```

***
# 14. 메시지, 국제화
### 14.1 메시지
* html파일에 메시지가 하드코딩 되어 있기 때문에, 단어 변경을 위해서 모든 파일을 변경해야한다.
* 아래와 같은 파일을 만들어 메시지 관리용 파일로 관리할 수 있다.
```
//message.properties
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```
* 그럼 아래와 같이 사용할 수 있다.
`<label for="itemName" th:text="#{item.itemName}"></label>`

### 14.2 국제화
* 나라별로 메시지를 관리할 수있다.

```
//messages_en.properties
item=Item
item.id=Item ID
item.itemName=Item Name
item.price=price
item.quantity=quantity
```
```
//messages_ko.properties
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```

### 14.3. 스프링 메시지 소스 설정
* 메시지 관리 기능을 사용하기 위해선 `MessageSoruce`를 스프링 빈으로 등록하면 된다.
* 구현체인 `ResourceBundleMessageSoruce`를 스프링 빈으로 등록하면된다.
``` java
@Bean
public MessageSource messageSource() {
  ResourceBundleMessageSource messageSource = new
  ResourceBundleMessageSource();
  messageSource.setBasenames("messages", "errors");
  messageSource.setDefaultEncoding("utf-8");
  return messageSource;
}
```
* `basenames` : 설정 파일의 이름을 지정한다.
  + `messages`로 지정하면 `messages.properties` 파일을 읽어서 사용한다.
  + 추가로 국제화 기능을 적용하려면 `messages_en.properties` , `messages_ko.properties` 와 같이 파일명 마지막에 언어 정보를 주면된다. 만약 찾을 수 있는 국제화 파일이 없으면 `messages.properties` (언어정보가 없는 파일명)를 기본으로 사용한다.
  + 파일의 위치는 `/resources/messages.properties` 에 두면 된다.
  + 여러 파일을 한번에 지정할 수 있다. 여기서는 `messages` , `errors` 둘을 지정했다.
 
* `defaultEncoding` : 인코딩 정보를 지정한다. `utf-8`을 사용한다.

##### 스프링 부트
스프링 부트를 사용하면 스프링부트가 `MessageSource`를 자동으로 스프링 빈으로 등록한다.


`application.properties`
```
spring.messages.basename=messages,config.i18n.messages
```
* 위와 같이 스프링 부트에서 메시지 소스를 설정할 수 있다.
* `MessageSource` 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 `messages` 라는 이름으로 기본 등록된다. 따라서 `messages_en.properties` , `messages_ko.properties` ,`messages.properties` 파일만 등록하면 자동으로 인식된다.

### 14.4. 스프링 메시지 소스 사용
##### 테스트 코드 설명
```java
@SpringBootTest
public class MessageSourceTest {
  @Autowired
  MessageSource ms;

  @Test
  void helloMessage() {
    String result = ms.getMessage("hello", null, null);
    assertThat(result).isEqualTo("안녕");
  }
}
```
* `ms.getMessage("hello", null, null)`
  + **code**: `hello`
  + **args**: `null`
  + **locale**: `null`
* `locale` 정보가 없으면 `basename`에서 설정한 기본 이름 메시지 파일을 조회한다.
* `basename`으로 `messages`를 지정했으므로 `messages.properties` 파일에서 데이터를 조회한다.

##### 메시지가 없는 경우, 기본메시지
``` java
```java
@Test
void notFoundMessageCode() {
  assertThatThrownBy(() -> ms.getMessage("no_code", null, null)).isInstanceOf(NoSuchMessageException.class);
}
@Test
void notFoundMessageCodeDefaultMessage() {
  String result = ms.getMessage("no_code", null, "기본 메시지", null);
  assertThat(result).isEqualTo("기본 메시지");
}
```
* 메시지가 없는 경우에는 `NoSuchMessageException`이 발생한다.
* 메시지가 없어도 기본메시지(`defaultMessage`) 매개변수를 사용하면 기본메시지가 반환된다.

##### 매개변수 사용
``` java
@Test
void argumentMessage() {
  String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
  assertThat(result).isEqualTo("안녕 Spring");
}
```
* 다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다.
* `hello.name=안녕 {0}` ➡️ Spring 단어를 매개변수로 전달 ➡️ `안녕 Spring`

##### 국제화 파일 선택
* locale 정보를 기반으로 국제화 파일을 선택한다.
* Locale이 `en_US`의 경우 `message_en_US` ➡️ `message_en` ➡️ `messages` 순서로 찾는다.
* `Locale`에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다고 이해하면 된다.

### 14.5. 웹 어플리케이션에 메시지 적용하기
##### messages.properties
```
label.item=상품
label.item.id=상품 ID
label.item.itemName=상품명
label.item.price=가격
label.item.quantity=수량

page.items=상품 목록
page.item=상품 상세
page.addItem=상품 등록
page.updateItem=상품 수정

button.save=저장
button.cancel=취소
```

##### 타임리프 메시지 적용
* #{...} : `#{label.item}`으로 적용
* 렌더링 전 : `<div th:text="#{label.item}"></div>`
* 렌더링 후 : `<div> 상품 </div>`

### 14.6. 웹 어플리케이션 국제화 적용하기
##### messages_en.properties
```
label.item=Item
label.item.id=Item ID
label.item.itemName=Item Name
label.item.price=price
label.item.quantity=quantity

page.items=Item List
page.item=Item Detail
page.addItem=Item Add
page.updateItem=Item Update

button.save=Save
button.cancel=Cancel
```

##### 스프링의 국제화 메시지 선택
* 스프링도 `Locale` 정보를 알아야 언어를 선택할 수 있다.
* 스프링은 언어 선택시 기본으로 `Accept-Language`헤더의 값을 사용한다.

##### LocaleResolver
* 스프링은 `Locale` 선택 방식을 변경할 수 있도록 `LocaleResolver` 라는 인터페이스를 제공하는데, 스프링 부트는 기본으로 `Accept-Language` 를 활용하는 `AcceptHeaderLocaleResolver` 를 사용한다.

***
# 15. 검증1 - Validation
### 15.1. 검증 직접 처리 로직 V1
![image](https://github.com/helloJosh/springmvc-itemservice-toy/assets/37134368/192cbce8-f3ed-47d9-b666-f45b57eb6002)
![image](https://github.com/helloJosh/springmvc-itemservice-toy/assets/37134368/fdf63b67-d647-48f5-b1ce-8f7b26b22809)
* 고객이 상품 등록 폼에서 상품명을 입력하지 않거나, 가격, 수량 등이 너무 작거나 커서 검증 범위를 넘어서면, 서버 검증 로직이  실패해야한다.
* 검증에 실패한 경우 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값을 잘못 입력했는지 친절하게 알려줘야한다.

### 15.2. 검즘 직접 처리 -개발
#### 15.2.1. 상품 등록 검증
##### ValidationItemControllerV1 - addItem()
```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
  //검증 오류 결과를 보관
  Map<String, String> errors = new HashMap<>();

  //검증 로직
  if (!StringUtils.hasText(item.getItemName())) {
    errors.put("itemName", "상품 이름은 필수입니다.");
  }
  if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
  }
  if (item.getQuantity() == null || item.getQuantity() > 9999) {
    errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
  }

  //특정 필드가 아닌 복합 룰 검증
  if (item.getPrice() != null && item.getQuantity() != null) {
    int resultPrice = item.getPrice() * item.getQuantity();
    if (resultPrice < 10000) {
      errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice);
    }
  }

  //검증에 실패하면 다시 입력 폼으로
  if (!errors.isEmpty()) {
    model.addAttribute("errors", errors);
    return "validation/v1/addForm";
  }

  //성공 로직
  Item savedItem = itemRepository.save(item);
  redirectAttributes.addAttribute("itemId", savedItem.getId());
  redirectAttributes.addAttribute("status", true);

  return "redirect:/validation/v1/items/{itemId}";
}
```
* 검증 오류 보관 : `Map<String, String> errors = new HashMap<>();`
* 특정 필드의 범위를 넘어서는 검증 로직 : `globalError`라는 `key` 사용
* 검증에 실패하면 다시 입력 폼으로 뷰 템플릿으로 보낸다.

##### addForm.html - 검증 추가
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <link th:href="@{/css/bootstrap.min.css}" href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
    .container {max-width: 560px;}
    .field-error {border-color: #dc3545;color: #dc3545;}
    </style>
  </head>

  <body>
    <div class="container">
    <div class="py-5 text-center">
      <h2 th:text="#{page.addItem}">상품 등록</h2>
    </div>
    <form action="item.html" th:action th:object="${item}" method="post">
      <div th:if="${errors?.containsKey('globalError')}">
        <p class="field-error" th:text="${errors['globalError']}">전체 오류 메시지</p>
      </div>
  
      <div>
        <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
        <input type="text" id="itemName" th:field="*{itemName}" th:class="${errors?.containsKey('itemName')} ? 'form-control field-error' : 'form-control'" class="form-control" placeholder="이름을 입력하세요">
        <div class="field-error" th:if="${errors?.containsKey('itemName')}" th:text="${errors['itemName']}">
        상품명 오류
        </div>
      </div>
  
      <div>
        <label for="price" th:text="#{label.item.price}">가격</label>
        <input type="text" id="price" th:field="*{price}" th:class="${errors?.containsKey('price')} ? 'form-control field-error' : 'form-control'" class="form-control" placeholder="가격을 입력하세요">
        <div class="field-error" th:if="${errors?.containsKey('price')}" th:text="${errors['price']}">
        가격 오류
        </div>
      </div>
  
      <div>
        <label for="quantity" th:text="#{label.item.quantity}">수량</label>
        <input type="text" id="quantity" th:field="*{quantity}" th:class="${errors?.containsKey('quantity')} ? 'form-control field-error' : 'form-control'" class="form-control" placeholder="수량을 입력하세요">
        <div class="field-error" th:if="${errors?.containsKey('quantity')}" th:text="${errors['quantity']}">
        수량 오류
        </div>
      </div>
  
      <hr class="my-4">
        <div class="row">
        <div class="col">
        <button class="w-100 btn btn-primary btn-lg" type="submit" th:text="#{button.save}">저장</button>
        </div>
        <div class="col">
        <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'" th:onclick="|location.href='@{/validation/v1/items}'|" type="button" th:text="#{button.cancel}">취소</button>
        </div>
      </div>
    </form>
    </div> <!-- /container -->
  </body>
</html>
```

* 글로벌 오류 메시지 : 오류 메시지는 `errors`에 내용이 있을 때만 출력된다. 타임리프의 `th:if`를 사용하면 조건에 만족할 때만 해당 HTML 태그를 출력할 수 있다.
> 참고 **Safe Navigation Operator** <br/>
> 등록폼에 진입한 시점에는 `errors`가 없기 때문에 `errors.containsKey()`를 호출하는 순간 `NullPointerException`이 발생한다
> `errors?.` 은 `errors` 가 `null` 일때 `NullPointerException` 이 발생하는 대신, `null` 을 반환하는 문법이다.
> 스프링 SpringEL이 제공하는 문법이다.

* 필드 오류 처리 : `classappend`을 사용해서 해당 필드에 오류가 있으면 `field-error` 라는 클래스 정보를 더해서 폼의 색깔을 빨간색으로 강조한다. 만약 값이 없으면 `_` (No-Operation)을 사용해서 아무것도 하지 않는다.
``` html
<input type="text" th:classappend="${errors?.containsKey('itemName')} ? 'fielderror' : _" class="form-control">
```
* 필드오류 처리 - 메시지 : 글로벌 오류 메시지에서 설명한 내용과 동일하고, 필드 오류를 대상으로 한다.

#### 15.2.2. 문제점
* 뷰 템플릿에서 중복 처리가 많다.
* 타임 오류 처리가 안된다. `Item`의 `price`, `quantity` 같은 숫자 필드는 타입이 Integer 이므로 문자 타입으로 설정하는 것이 불가능하다. 또한 숫자 타입에 문자가 들어오면 400 예외가 발생하며 오류페이지를 띄운다
* `Item` 의 `price` 에 문자를 입력하는 것 처럼 타입 오류가 발생해도 고객이 입력한 문자를 화면에 남겨야 한다.
* 결국 고객이 입력한 값도 어딘가에 별도로 관리가 되어야 한다.

### 15.3. 오류검증 V2
#### 15.3.1. BindingResult1
##### ValidationItemControllerV2 - addItemV1
```java
@PostMapping("/add")
public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
  if (!StringUtils.hasText(item.getItemName())) {
    bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
  }
  if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >1000000) {
    bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
  }
  if (item.getQuantity() == null || item.getQuantity() >= 10000) {
    bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
  }
  //특정 필드 예외가 아닌 전체 예외
  if (item.getPrice() != null && item.getQuantity() != null) {
    int resultPrice = item.getPrice() * item.getQuantity();
    if (resultPrice < 10000) {
      bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
    }
  }
  if (bindingResult.hasErrors()) {
    log.info("errors={}", bindingResult);
    return "validation/v2/addForm";
  }
  //성공 로직
  Item savedItem = itemRepository.save(item);
  redirectAttributes.addAttribute("itemId", savedItem.getId());
  redirectAttributes.addAttribute("status", true);
  return "redirect:/validation/v2/items/{itemId}";
}
```
> **주의** `BindingResult bindingResult` 파라미터의 위치는 `@ModelAttribute Item item` 다음에 와야한다.
* FieldError 생성자 요약 : `public FieldError(String objectName, String field, String defaultMessage) {}`
* 필드에 오류가 있으면 `FieldError` 객체를 생성해서 `bindingResult` 에 담아두면 된다.
  + `objectName` : `@ModelAttribute` 이름
  + `field`: 오류가 발생한 필드 이름
  + `defaultMessage`: 오류 기본 메시지
 
* 글로벌 오류 - OjbectError : `bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 = " + resultPrice));`
* ObjectError 생성자 요약 : `public ObjectError(String objectName, String defaultMessage) {}`
  + `objectName` : `@ModelAttribute`의 이름
  + `defaultMessage` : 오류 기본 메시지
 
```html
<form action="item.html" th:action th:object="${item}" method="post">
  <div th:if="${#fields.hasGlobalErrors()}">
    <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">글로벌 오류 메시지</p>
  </div>

  <div>
    <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}" th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">
    <div class="field-error" th:errors="*{itemName}">
      상품명 오류
    </div>
  </div>

  <div>
    <label for="price" th:text="#{label.item.price}">가격</label>
    <input type="text" id="price" th:field="*{price}" th:errorclass="field-error" class="form-control" placeholder="가격을 입력하세요">
    <div class="field-error" th:errors="*{price}">
      가격 오류
    </div>
  </div>

  <div>
    <label for="quantity" th:text="#{label.item.quantity}">수량</label>
    <input type="text" id="quantity" th:field="*{quantity}" th:errorclass="field-error" class="form-control" placeholder="수량을 입력하세요">
    <div class="field-error" th:errors="*{quantity}">
      수량 오류
    </div>
  </div>
```
##### 타임리프 스프링 검증 오류 통합 기능
* `#fields` : `#fields`로 `BindingResult`가 제공하는 검증 오류에 접근할 수 있다.
* `th:errors`: 해당 필드에 오류가 있는 경우에 태그를 출력한다. `th:if`의 편의 버전이다.
* `th:errorclass`: `th:field`에서 지정한 필드에 오류가 있으면 `class` 정보를 추가한다.


### 15.4. BindingResult2
* 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다.
* `BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!

##### @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
* `BindingResultl`가 없으면 ➡️ 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
* `BindingResultl`가 있으면 ➡️ 오류정보(`FieldError`)를 `BindingResult`에 담아서 컨트롤러를 정상 호출한다.

##### BindingResult에 검증 오류를 적용하는 3가지 방법
* `@ModelAttribute`의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 `FieldError` 생성해서 `BindingResult`에 넣어준다
* 개발자가 직접 넣어준다
* `Validator`사용 ➡️ 뒤에 설명

##### 타입 오류 확인
* 숫자가 입력되어야할 곳에 문자를 입력해서 타입을 다르게해 `BindingResult`를 호출하고 `bindingResult`의 값을 확인해보자

> 주의! <br/>
> `BindingResult`는 검증할 대상 바로 다음에 와야한다. 예를 들어 `@ModelAttribute Item item` 뒤에 바로 `BindingResutl`가 와야한다. <br/>
> `BindingResult`는 Model에 자동으로 포함된다.

##### BindingResult와 Errors
* `org.springframework.validation.Errors`
* `org.springframework.validation.BindingResult `
* `BindingResult`는 인터페이스고 `Errors` 인터페이스를 상속받고 있다.
* 구현체는 `BeanPropertyBindingResult`이다. 둘다 구현하고 있어서 `BindingResult` 대신에 `Errors`를 사용해도 된다.
* `Errors` 인터페이스는 단순한 오류저장과 조회기능을 제공하지만 `BindingResult`는 추가적인 기능을 제공한다.
* 관례상 `BindingResult`를 많이 사용한다.

### 15.4. FieldError, ObjectError
* 오류가 나올 시 입력한 값이 남아 있게해야한다.
* 이것을 `FieldError`, `ObjectError`로 해결할 수 있다.

```java
@PostMapping("/add")
public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
  if (!StringUtils.hasText(item.getItemName())) {
    bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
  }
  if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
  }
  if (item.getQuantity() == null || item.getQuantity() >= 10000) {
    bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
  }
  //특정 필드 예외가 아닌 전체 예외
  if (item.getPrice() != null && item.getQuantity() != null) {
    int resultPrice = item.getPrice() * item.getQuantity();
    if (resultPrice < 10000) {
      bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
    }
  }
  if (bindingResult.hasErrors()) {
    log.info("errors={}", bindingResult);
    return "validation/v2/addForm";
  }
  //성공 로직
  Item savedItem = itemRepository.save(item);
  redirectAttributes.addAttribute("itemId", savedItem.getId());
  redirectAttributes.addAttribute("status", true);
  return "redirect:/validation/v2/items/{itemId}";
}
```
##### FieldError 생성자
* `public FeildError(String objectName, String field, String defaultMessage);`
* `public FeildError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage);

##### 파라미터 목록
* `objectName` : 오류가 발생한 객체 이름
* `field` : 오류 필드
* `rejectedValue` : 사용자가 입력한 값(거절된 값)
* `bindingFailure` : 타입오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
* `codes` : 메시지 코드
* `arguments` : 메시지에서 사용하는 인자
* `defaultMessage` : 기본 오류 메시지
> `ObjectError`도 유사하게 두가지 생성자를 제공한다.

### 15.5. 오류코드와 메시지 처리1
##### errors 메시지 파일 생성
* `messages.properties`를 사용해도 되지만, 오류 메시지를 구분하기 쉽게 `errors.properties`라는 별도의 파일에 관리한다.
##### application.properties 설정 추가
`spring.messages.basename=messages,errors`
##### errors.properties 추가
```
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```
> errors_en.properties를 통해 국제화 처리도 가능하다.

##### ValidationItemControllerV2 - addItemV3()
``` java
@PostMapping("/add")
public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
  if (!StringUtils.hasText(item.getItemName())) {
    bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
  }
  if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
  }
  if (item.getQuantity() == null || item.getQuantity() > 10000) {
    bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[] {9999}, null));
  }
  //특정 필드 예외가 아닌 전체 예외
  if (item.getPrice() != null && item.getQuantity() != null) {
    int resultPrice = item.getPrice() * item.getQuantity();
    if (resultPrice < 10000) {
      bindingResult.addError(new ObjectError("item", new String[] {"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
    }
  }
  if (bindingResult.hasErrors()) {
    log.info("errors={}", bindingResult);
    return "validation/v2/addForm";
  }
  //성공 로직
  Item savedItem = itemRepository.save(item);
  redirectAttributes.addAttribute("itemId", savedItem.getId());
  redirectAttributes.addAttribute("status", true);
  return "redirect:/validation/v2/items/{itemId}";
}
```

### 15.6. 오류코드와 메시지 처리2
