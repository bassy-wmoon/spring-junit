package io.github.bassy.wmoon.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import io.github.bassy.wmoon.app.repository.DemoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRepositoryTest {
	
	@Autowired
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
		new DbSetup(destination, Operations.truncate("test")).launch();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("after");
		new DbSetup(destination, Operations.truncate("test")).launch();
	}

	@Test
	public void test() {
		// setup テストデータ投入
		Operation insert = Operations.sequenceOf(
				Operations.insertInto("test")
					.columns("id", "name")
					.values(1, "hoge")
					.values(2, "fuga")
					.build(),
				Operations.insertInto("test")
					.row()
						.column("id", 3)
						.column("name", "piyo")
						.end()
					.build()
		);
		DbSetup dbSetup = new DbSetup(destination, insert);
		dbSetup.launch();
		
		// expect
		assertEquals(3, demoRepository.count());		
	}

}
