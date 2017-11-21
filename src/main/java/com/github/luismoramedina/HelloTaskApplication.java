package com.github.luismoramedina;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@EnableTask
@SpringBootApplication
public class HelloTaskApplication {

	/**
	 * I need these property to launch the task
	 */
	@Value("${sleep}")
	private Integer sleep;

	public static void main(String[] args) {
		SpringApplication.run(HelloTaskApplication.class, args);
	}

	@Bean
	public SimpleTask timeStampTask() {
		return new SimpleTask();
	}

	/**
	 * A commandline runner that prints some data.
	 */
	public class SimpleTask implements CommandLineRunner {

		@Override
		public void run(String... strings) throws Exception {
			System.out.println("here we call the service to do something!");
			for (int i = 0; i < 10; i++) {
				Thread.sleep(sleep);
				System.out.println("Do... " + i);
			}
			if (new Date().getTime() % 2 == 0) {
				System.err.println("simulate an error");
				System.exit(1);
			}
		}
	}


	@BeforeTask
	public void before(TaskExecution taskExecution) {
		System.out.println("This is before: " + taskExecution.getExecutionId());
	}

	@AfterTask
	public void after(TaskExecution taskExecution) {
		System.out.println("This is after: " + taskExecution.getExecutionId());

	}
}
