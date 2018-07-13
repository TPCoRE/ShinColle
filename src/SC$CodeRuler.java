import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * ASM Helper
 * */
final class SC$CodeRuler {
	
	/**
	 * 将classbuf以ShinColle的标准规范化
	 * */
	static final byte[] codefix(byte[] classbuf, String name) {
		
		//special handle
		if("net/minecraft/client/Minecraft".equals(name)) {
			ClassNode cn = SC$CodeRuler.read(classbuf);
			MethodNode run = SC$CodeRuler.find("run", "()V", cn.methods.iterator());
			
			//check&prepare
			if(run == null) throw new NoSuchMethodError("net.minecraft.client.Minecraft.run()V wasn't be Found!");
			InsnList ns = new InsnList();
			LabelNode l0 = new LabelNode();
			
			//coding nclient starter
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "gc", "()V"));
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "session", "Lnet/minecraft/util/Session;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile", "()Lcom/mojang/authlib/GameProfile;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/entity/player/EntityPlayer", "getUUID", "(Lcom/mojang/authlib/GameProfile;)Ljava/util/UUID;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "SC$Starter", "safeLaunch", "(Ljava/util/UUID;)Z"));
			ns.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			ns.add(new InsnNode(Opcodes.ICONST_0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "exit", "(I)V"));
			ns.add(l0); //nclient launch faild, steve client continue to run
			
			//inject&write
			run.instructions.insert(ns);
			return SC$CodeRuler.write(cn);
		}
		
		//null表示没修改
		return null;
	}
	
	/**
	 * toArrayByte
	 * */
	static final byte[] write(ClassNode cn) {
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		
		return cw.toByteArray();
	}
	
	/**
	 * Create ClassNode from byte[]
	 * */
	static final ClassNode read(byte[] b) {
		ClassReader cr = new ClassReader(b);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		return cn;
	}
	
	/**
	 * 搜索特殊方法
	 * */
	static final MethodNode find(String name, String desc, Iterator<MethodNode> mns) {
		while(mns.hasNext()) {
			MethodNode mn = mns.next();
			
			if(mn.name.equals(name) && mn.desc.equals(desc)) return mn;
		}
		
		//no found
		return null;
	}
}
