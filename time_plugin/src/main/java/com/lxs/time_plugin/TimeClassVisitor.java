package com.lxs.time_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author liuxiaoshuai
 * @date 2019-08-17
 * @desc
 * @email liulingfeng@mistong.com
 */
public class TimeClassVisitor extends ClassVisitor implements Opcodes {
    private String mClassName;

    public TimeClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        //desc方法的描述符 access方法的访问标志
        if ("<init>".equals(name)) {
            return mv;
        }
        return new MethodAdapterVisitor(mv, access, name, desc, mClassName);
    }
}
