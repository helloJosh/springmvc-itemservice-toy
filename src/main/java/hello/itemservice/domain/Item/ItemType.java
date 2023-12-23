package hello.itemservice.domain.Item;

public enum ItemType {
    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
