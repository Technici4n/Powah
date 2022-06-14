package owmii.powah.config.v2.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LongRange {
	long min();
	long max();
}
