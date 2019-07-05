import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author liuxiaoshuai
 * @date 2019-07-05
 * @desc
 * @email liulingfeng@mistong.com
 */
public class MethodAdapterVisitor extends AdviceAdapter {
    private boolean inject;
    private MethodVisitor mv;
    private int start, end;

    protected MethodAdapterVisitor(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.mv = mv;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Type.getDescriptor(InjectTime.class).equals(desc)) {
            System.out.println(desc);
            inject = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    /**
     * 执行插装
     */

    @Override
    protected void onMethodEnter() {
        if (inject) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            start = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, start);
        }

    }

    @Override
    protected void onMethodExit(int opcode) {
        if (inject) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            end = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, end);


        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("execute");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(LLOAD, end);
        mv.visitVarInsn(LLOAD, start);
        mv.visitInsn(LSUB);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder", false);
        mv.visitLdcInsn(".ms");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}
