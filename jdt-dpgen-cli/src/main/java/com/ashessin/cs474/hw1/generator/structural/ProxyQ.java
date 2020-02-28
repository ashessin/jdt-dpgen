package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "proxy", version = "jdt-dpgen 0.1",
		description = "Generates Proxy structural design pattern. " +
					  "Provide a surrogate or placeholder for another object to control access to it.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class ProxyQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = LoggerFactory.getLogger(ProxyQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.proxy";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new ProxyQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String proxyName = InputGroup.proxyName;
		String subjectName = InputGroup.subjectName;
		String realSubjectName = InputGroup.realSubjectName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new ProxyGen(packageName, proxyName, subjectName, realSubjectName).main();

	}

	static class InputGroup {

		private static final String PROXY_NAME = "Proxy";
		private static final String SUBJECT_NAME = "Subject";
		private static final String REAL_SUBJECT_NAME = "Real" + SUBJECT_NAME;
		@CommandLine.Parameters(index = "0", paramLabel = "ProxyName",
				description = "The Proxy class keeps reference to the real subject: it can act as a surrogate, " +
							  "controlling access to the real subject and can be responsible for creation " +
							  "and maintenance of the Real subject.")
		static String proxyName = PROXY_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "SubjectName",
				description = "The Subject interface defines a common interface for the RealSubject " +
							  "and the Proxy.")
		static String subjectName = SUBJECT_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "RealSubjectName",
				description = "The RealSubject class points to the real object which the Proxy represents.")
		static String realSubjectName = REAL_SUBJECT_NAME;

		private InputGroup() {
		}
	}
}
