# Todo-list

## 기능목록

- 회원가입
- Todo 생성
- Todo 조회
    - 목록 조회
    - 상세 조회
- Todo 수정
- Todo 상태 변경
- Todo 검색

## API 정의서

### 회원가입

> [POST] /api/members   
> 로그인 아이디가 존재 할 경우 예외가 발생한다.   
> 아이디, 비밀번호를 입력하지 않을 경우 예외가 발생한다.

- #### RequestBody

```json
{
  "email": "tao@example.com",
  "password": "1234"
}
```

- #### ResponseBody

```json
{
  "id": 1,
  "email": "tao@example.com",
  "createdAt": "2024-09-14 14:35:33",
  "updatedAt": "2024-09-14 14:35:33"
}
```

### Todo 생성

> [POST] /api/todos  
> 제목을 입력하지 않을 경우 예외가 발생한다.  
> 생성 시 default 상태값은 'TODO'를 갖는다.

- #### RequestBody

```json
{
  "userId": 1,
  "title": "todo title",
  "content": "todo content"
}
```

- #### ResponseBody

```json
{
  "id": 1,
  "title": "todo title",
  "content": "todo content",
  "status": "TODO",
  "createdAt": "2024-09-14 14:35:33",
  "updatedAt": "2024-09-14 14:35:33",
  "member": {
    "id": 1,
    "email": "tao@example.com"
  }
}
```

### Todo 검색

> [GET] /api/todos?memberId=3&title=hello&status=TODO  
> memberId 값이 존재하지 않으면 예외가 발생한다.   
> title이 포함되어있는(like) todo를 응답한다.  
> title은 nullable 할 수 있다.

- #### ResponseBody

```json
{
  "contents": [
    {
      "title": "todo title1",
      "status": "TODO",
      "createdAt": "2024-09-14 14:35:33",
      "updatedAt": "2024-09-14 14:35:33"
    },
    {
      "title": "todo title2",
      "status": "DONE",
      "createdAt": "2024-09-14 14:35:33",
      "updatedAt": "2024-09-14 14:35:33"
    },
    {
      "title": "todo title3",
      "status": "DONE",
      "createdAt": "2024-09-14 14:35:33",
      "updatedAt": "2024-09-14 14:35:33"
    }
  ]
}
```

> 상세 조회   
> [GET] /api/todos/{todoId}

- #### ResponseBody

```json
{
  "title": "todo title",
  "content": "todo content",
  "status": "TODO",
  "createdAt": "2024-09-14 14:35:33",
  "updatedAt": "2024-09-14 14:35:33"
}
```

### Todo 수정

> [PUT] /api/todos/{todoId}  
> 제목을 입력하지 않을 경우 예외가 발생한다.
> content 값이 없으면 null로 업데이트한다.

- #### RequestBody

```json
{
  "title": "update title",
  "content": "todo content",
  "status": "TODO"
}
```

- #### ResponseBody

```json
{
  "id": 1,
  "title": "update title",
  "content": "todo content",
  "status": "TODO",
  "createdAt": "2024-09-14 14:35:33",
  "updatedAt": "2024-09-19 16:21:18"
}
```

### Todo 상태 변경

> [PUT] /api/todos/status/{todoId}  
> status 값이 없으면 예외가 발생한다.

- #### RequestBody

```json
{
  "status": "DONE"
}
```

- #### ResponseBody

```json
{
  "id": 1,
  "title": "todo title",
  "content": "todo content",
  "status": "DONE"
}
```
