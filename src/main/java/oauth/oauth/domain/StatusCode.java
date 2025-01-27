package oauth.oauth.domain;

public enum StatusCode {
    CREATED(200),
    UPDATED(201),
    DELETED;

    int statusCode;
    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    StatusCode(){
        System.out.println("Hello~");
    }
}
