import org.junit.Test;
import org.objectweb.asm.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author liuxiaoshuai
 * @date 2019-07-05
 * @desc
 * @email liulingfeng@mistong.com
 */
public class InjectTimeUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        /**
         * 读取文件
         */
        FileInputStream fis = new FileInputStream("/Users/liuxiaoshuai/Desktop/AOP/app/src/test/java/InjectTest.class");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int length;
        while ((length = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }

        byte[] sourceClass = bos.toByteArray();

        ClassReader classReader = new ClassReader(sourceClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classReader.accept(new ClassAdapterVisitor(classWriter), ClassReader.EXPAND_FRAMES);

        /**
         * 输出
         */
        byte[] newClass = classWriter.toByteArray();
        File file = new File("/Users/liuxiaoshuai/Desktop/AOP/app/src/test/java2/");
        file.mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/liuxiaoshuai/Desktop/AOP/app/src/test/java2/InjectTest.class");
        fileOutputStream.write(newClass);

        bos.close();
        fileOutputStream.close();
        fis.close();
    }

    static class ClassAdapterVisitor extends ClassVisitor {

        public ClassAdapterVisitor(ClassVisitor cv) {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            return new MethodAdapterVisitor(mv, access, name, desc);
        }
    }
}
