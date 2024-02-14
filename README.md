﻿# 스프링 MVC 1,2편 토이프로젝트 내용 정리
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

