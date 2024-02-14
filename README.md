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









