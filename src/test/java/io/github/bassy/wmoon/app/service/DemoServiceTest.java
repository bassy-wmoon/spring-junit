package io.github.bassy.wmoon.app.service;

import io.github.bassy.wmoon.app.model.User;
import io.github.bassy.wmoon.app.repository.DemoRepository;
import io.github.bassy.wmoon.app.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class DemoServiceTest {

	private DemoService demoservice;

	private DemoRepository demoRepository;

	private UserRepository userRepository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("before class");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("after class");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("before");
		userRepository = mock(UserRepository.class);
		demoRepository = mock(DemoRepository.class);
		demoservice = new DemoService(userRepository, demoRepository);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("after");
	}

	@Test
	public void ユーザリポジトリのinsertUserが1回実行されること() {

		User user = new User();
		user.setName("Mike");
		user.setSex(User.Sex.MALE);

		// Mock
		doNothing().when(userRepository).insertUser(any());

		demoservice.saveUser(user);

		verify(userRepository, times(1)).insertUser(any());
	}

}
