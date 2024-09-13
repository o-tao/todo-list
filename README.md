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
  "password": "1234",
  "createdAt": "2024-09-14",
  "updatedAt": "2024-09-14"
}
```

### Todo 생성

> POST   
> /api/todos

> 제목을 입력하지 않을 경우 예외가 발생한다.
> 생성 시 default 상태값은 '할일'을 갖는다.

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
  "createdAt": "2024-09-14",
  "updatedAt": "2024-09-14",
  "user": {
    "id": 1,
    "email": "tao@example.com"
  }
}
```

### Todo 조회

> 목록 조회   
> GET   
> /api/todos

- #### ResponseBody

```json
{
  "todos": [
    {
      "title": "todo title1",
      "status": "TODO"
    },
    {
      "title": "todo title2",
      "status": "DONE"
    },
    {
      "title": "todo title3",
      "status": "DONE"
    }
  ]
}
```

> 상세 조회   
> GET   
> PathVariable   
> /api/todos/{todoId}

- #### ResponseBody

```json
{
  "title": "todo title",
  "content": "todo content",
  "status": "TODO"
}
```

### Todo 수정

> PUT   
> PathVariable   
> /api/todos/{todoId}

> 제목을 입력하지 않을 경우 예외가 발생한다.

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
  "updatedAt": "2024-09-15"
}
```

### Todo 상태 변경

> PATCH   
> PathVariable   
> /api/todo/status/{todoId}

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

### Todo 검색

> GET   
> Parameter   
> /pai/todo?search=todo

- #### ResponseBody
```json
{
  "todos": [
    {
      "title": "todo title1",
      "status": "TODO"
    },
    {
      "title": "todo title2",
      "status": "DONE"
    },
    {
      "title": "todo title3",
      "status": "CANCEL"
    }
  ]
}
```