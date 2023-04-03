# junit

junit은 테스트를 위한 라이브러리로 테스트 메서드를 실행 후 종료(트랜잭션 실행 후 종료) 시 Rollback이 된다.

test시 순서 : repository -> service -> controller
repository test시 DB 관련 테스트를 진행하고
service에선 기능들이 트랜잭션을 잘 타는지 확인하
null check 관련해서는 controller에서 클라이언트와 테스트시에 확인

테스트는 스프링부트를 실행하지 않고 별도의 테스트 환경을 실행
메모리(repository load)만 테스트한다면 단위 테스트를 진행 (repository 테스트시 @Datajpatest 어노테이션 사용)

컨트롤러 title, author -> BookSaveReqDto -> Service -> Book 엔티티 변환 -> BookRepository.save(book)

BookRepository.save(book)
-> DB에 저장 (primary key 생성 완료 = id 생성 완료)
-> save 메서드가 DB에 저장된 Book을 return (DB 데이터와 동기화된 데이터)

assertions 관련 메소드 사용시 자동완성기능에 문제가 발생하므로 직접 다 친뒤 static으로 import

junit 테스트는 각 메서드 순서를 보장하지 않는다. -> @Order 어노테이션을 통해 순서를 결정

각 테스트 메서드는 테스트 실행 후 데이터 초기화 -> @DataJpaTest내의 @Transactional로 각 메서드에 트랙잭션을 설정, 하지만 primary key에 auto_increment된 값은 초기화가 안됨
해결방법으로는 @Sql("classpath:db/tableInit.sql")을 id를 찾는 메소드마다 설정해줘서 메소드 전에 초기화를 진행

@sql 어노테이션으로 테이블을 드랍하면 메모리에 있는 데이터를 삭제되는 것이 아닌 하드디스크에 저장된 내용을 삭제한다.
@BeforeEach 어노테이션을 통해 넣어줬던 데이터는 메모리에 들어가기에 삭제되지 않고 메모리에 남아있다가 테스트 메소드에서 요청이 메모리에 있던 데이터가 사용된다.
그 후 테스트 메소드가 종료되면 트랙잭션으로 인해 롤백되어 메모리에 있던 데이터도 삭제된다.

실제 서버로 테스트할 경우 id를 통한 검증을 제외한다.

mock를 사용할 경우 @ExtendWith(MockitoExtension.class)를 클래스 어노테이션으로 추가하는 대신 @DataJpaTest를 삭제한다.

책 목록보기 반환 타입을 List로 하기 보다는 BookRespDto를 List타입으로 받는 dto를 추가로 만들어 주는것이 좋다.

jdbc sql 예외 오류를 보면 repository나 db쪽 문제로 볼 수 있지만 클라이언트에서 잘못보낸 데이터를 컨트롤러 레이어에서 걸러주지 않으면 이런 오류가 발생할 수 있으므로 
각 레이어의 역할분담을 잘 하여 정확한 오류 진단을 가능케 해준다.
