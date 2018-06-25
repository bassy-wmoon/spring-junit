package io.github.bassy.wmoon.app.repository;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRepositoryTest {

	@Autowired
	private DataSource dataSource;

	private Destination destination;
	
	@Autowired	
	private DemoRepository demoRepository;
    
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
		destination = getDestination();
		new DbSetup(destination, Operations.truncate("user")).launch();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("after");
		new DbSetup(destination, Operations.truncate("user")).launch();
	}

	@Test
	public void test() {
		// setup テストデータ投入
		Operation insert = Operations.sequenceOf(
				Operations.insertInto("user")
					.columns("id")
					.values(1)
					.values(2)
					.build(),
				Operations.insertInto("user")
					.row()
						.column("id", 3)
					.end()
					.build()
		);
		DbSetup dbSetup = new DbSetup(destination, insert);
		dbSetup.launch();
		
		// expect
		assertEquals(3, demoRepository.count());		
	}

	private Destination getDestination() {
		return new DataSourceDestination(this.dataSource);
	}
}
