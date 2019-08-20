package com.lxs.time_plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author liuxiaoshuai
 * @date 2019-08-20
 * @desc
 * @email liulingfeng@mistong.com
 */
public class MethodAdapterVisitor extends AdviceAdapter {
    private MethodVisitor mv;
    private int start;
    private String methodName, className;

    MethodAdapterVisitor(MethodVisitor methodVisitor, int access, String name, String desc, String className) {
        super(Opcodes.ASM5, methodVisitor, access, name, desc);
        mv = methodVisitor;
        this.methodName = name;
        this.className = className;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Type.getDescriptor(InjectTime.class).equals(desc)) {

        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    protected void onMethodEnter() {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        start = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, start);//访问本地变量
    }

    @Override
    protected void onMethodExit(int i) {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        int end = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, end);
        mv.visitLdcInsn(className);//访问常量
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");//访问类型指令
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn(methodName + "执行时间");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(Opcodes.LLOAD, end);
        mv.visitVarInsn(Opcodes.LLOAD, start);
        mv.visitInsn(Opcodes.LSUB);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }
}
