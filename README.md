# junit

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
해결방법으로는 @Sql("classpath:db/tableInit.sql")을 id를 찾는 메소드마다 설정해줘서 메소드 전에 초기화를 진
