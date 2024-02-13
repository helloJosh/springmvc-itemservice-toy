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

# 2. 서비스 제공 흐름
![image](https://github.com/helloJosh/spring-item-service-study/assets/37134368/398c219c-b74a-4832-8c07-c48d5b2b8373)

# 3. 상품 도메인 개발
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

