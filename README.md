# stomp_chat
9oormthonuniv_backend_study 

실시간 채팅을 stomp 웹소켓을 이용해서 구현 (백엔드 기능만) <br/><br/>

## 실시간 채팅 ERD
- 카카오톡과 같은 소셜미디어를 보고 어떠한 기능이 필요한 지를 생각해서 ERD를 작성해보았다.
- *참고
- joinChatting table 같은 경우에는 User와 ChattingRoom의 중간 테이블이다.
- 중간 테이블을 둔 이유는 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없기 때문에 연결 테이블을 중간에 추가해서 다대일 관계로 풀어내는게 바람직한 해결 방법이기에 joinChatting 테이블을 두었다.

https://www.erdcloud.com/d/D9zeB2rjLnv7eggqA
