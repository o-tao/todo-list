# Todo-list

## 기능목록

- 회원가입
- 로그인
- 회원탈퇴
- Todo 생성
- Todo 조회
    - 목록 조회
    - 상세 조회
- Todo 수정
- Todo 상태 변경
- Todo 검색

## API 정의서

### 회원가입

> POST   
> /api/members

> 로그인 아이디가 존재 할 경우 예외가 발생한다.
> 아이디, 비밀번호를 입력하지 않을 경우 예외가 발생한다.

```json
{
  "loginId": "로그인아이디",
  "password": "비밀번호"
}
```

### 로그인

> POST   
> /api/members/login

> 아이디, 비밀번호가 일치하지 않을 경우 에외가 발생한다.

```json
{
  "loginId": "로그인아이디",
  "password": "비밀번호"
}
```

### 로그아웃

> POST
> /api/members/logout

### 회원탈퇴

> DELETE   
> PathVariable   
> /api/member/{memberId}

> 회원이 존재하지 않을 경우 예외가 발생한다.

### Todo 생성

> POST   
> /api/todos

> 제목을 입력하지 않을 경우 예외가 발생한다.

```json
{
  "title": "제목",
  "content": "내용",
  "createdAt": "생성날짜",
  "updatedAt": "수정날짜"
}
```

### Todo 조회

> 목록 조회   
> GET   
> /api/todos

> 상세 조회   
> GET   
> PathVariable   
> /api/todos/{todoId}

### Todo 수정

> PUT   
> PathVariable   
> /api/todos/{todoId}

> 제목을 입력하지 않을 경우 예외가 발생한다.

### Todo 상태 변경

> PATCH   
> PathVariable   
> /api/todo/{todoId}

### Todo 검색

> GET   
> Parameter   
> /pai/todo?search=