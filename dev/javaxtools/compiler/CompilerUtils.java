package javaxtools.compiler;

import java.util.Arrays;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class CompilerUtils {

	public static Map<String, Class<Object>> compile(
			Map<String, CharSequence> map, StringBuilder sb) {
		CharSequenceCompiler<Object> csc = new CharSequenceCompiler<Object>(
				Thread.currentThread().getContextClassLoader(), Arrays
						.asList(new String[] { "-verbose" }));

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

		Map<String, Class<Object>> c = null;
		try {
			c = csc.compile(map, diagnostics);
		} catch (CharSequenceCompilerException e) {
			for (Diagnostic<? extends JavaFileObject> diagnostic : e
					.getDiagnostics().getDiagnostics()) {
				// System.err.println(diagnostic);
				sb.append(diagnostic).append("\n");
			}
		}

		return c;
	}

}
