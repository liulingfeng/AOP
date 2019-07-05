import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxiaoshuai
 * @date 2019-07-05
 * @desc
 * @email liulingfeng@mistong.com
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface InjectTime {
}
