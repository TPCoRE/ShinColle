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
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * ASM Helper
 * */
final class SC$CodeRuler {
	
	/**
	 * 将classbuf以ShinColle的标准规范化
	 * */
	static final byte[] codefix(byte[] classbuf, String name) {
		if(name == null || classbuf == null) return null;
		ClassNode cn = null;
		
		//special handle
		switch(name) {
		case "net/minecraft/client/Minecraft": //nclient starter
			cn = SC$CodeRuler.read(classbuf);
			MethodNode mn0 = SC$CodeRuler.find("run", "()V", cn.methods.iterator());
			MethodNode mn1 = SC$CodeRuler.find("startGame", "()V", cn.methods.iterator());
			
			//check&prepare mn0
			if(mn0 == null) throw new NoSuchMethodError("net.minecraft.client.Minecraft.run()V wasn't be Found!");
			InsnList ns = new InsnList();
			LabelNode l0 = new LabelNode();
			
			//coding nclient starter(Bootstrap.register() has run, 物品已经注册，模型，贴图尚未加载，nclient只会用mcid->具体物品或方块->NCLIENT_ENTITY)
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/init/Bootstrap", "register", "()V"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "gc", "()V"));
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "session", "Lnet/minecraft/util/Session;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/Session", "getProfile", "()Lcom/mojang/authlib/GameProfile;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/entity/player/EntityPlayer", "getUUID", "(Lcom/mojang/authlib/GameProfile;)Ljava/util/UUID;"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/shincolle/nclient/NClient", "launch", "(Ljava/util/UUID;)Z"));
			ns.add(new JumpInsnNode(Opcodes.IFEQ, l0));
			ns.add(new InsnNode(Opcodes.ICONST_0));
			ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "SC$Starter", "exit", "(I)V"));
			ns.add(l0); //nclient launch faild, steve client continue to run
			
			//inject mn0
			mn0.instructions.insert(ns);
			
			//check&prepare mn1
			if(mn1 == null) throw new NoSuchMethodError("net.minecraft.client.Minecraft.startGame()V wasn't be Found!");
			ns = new InsnList();
			
			//coding resourcepack injecter(为steve client注册贴图)
			ns.add(new VarInsnNode(Opcodes.ALOAD, 0));
			ns.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "defaultResourcePacks", "Ljava/util/List;"));
			ns.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/client/resources/FolderResourcePack"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new TypeInsnNode(Opcodes.NEW, "java/io/File"));
			ns.add(new InsnNode(Opcodes.DUP));
			ns.add(new LdcInsnNode(SC$Starter.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V"));
			ns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/resources/FolderResourcePack", "<init>", "(Ljava/io/File;)V"));
			ns.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z"));
			ns.add(new InsnNode(Opcodes.POP));
			
			//inject mn1
			mn1.instructions.insert(ns);
			break;
		case "net/minecraft/item/Item": //next
		case "net/minecraft/block/Block": //register 多核金属, 深渊金属
			cn = SC$CodeRuler.read(classbuf);
			mn0 = SC$CodeRuler.find("registerBlocks", "()V", cn.methods.iterator());
			
			//check&prepare
			boolean flag = mn0 == null;
			if(flag) mn0 = SC$CodeRuler.find("registerItems", "()V", cn.methods.iterator());
			if(mn0 == null) throw new NoSuchMethodError("registerBlocks()V & registerItems()V wasn't be Found!");
			ns = new InsnList();
			
			//coding
			if(!flag) {
				//coding myblocks register
				ns.add(new LdcInsnNode(new Integer(218)));
				ns.add(new LdcInsnNode("shincolle:polymetal"));
				ns.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/shincolle/nserver/Blocks", "POLYMETAL", "Lnet/minecraft/block/Block;"));
				ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/block/Block", "registerBlock", "(ILjava/lang/String;Lnet/minecraft/block/Block;)V"));
				ns.add(new LdcInsnNode(new Integer(219)));
				ns.add(new LdcInsnNode("shincolle:abyssmetal"));
				ns.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/shincolle/nserver/Blocks", "ABYSSMETAL", "Lnet/minecraft/block/Block;"));
				ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/block/Block", "registerBlock", "(ILjava/lang/String;Lnet/minecraft/block/Block;)V"));
				
				//inject
				mn0.instructions.insert(ns);
			} else {
				//coding myitems register
				ns.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/shincolle/nserver/Blocks", "POLYMETAL", "Lnet/minecraft/block/Block;"));
				ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/item/Item", "registerItemBlock", "(Lnet/minecraft/block/Block;)V"));
				ns.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/shincolle/nserver/Blocks", "ABYSSMETAL", "Lnet/minecraft/block/Block;"));
				ns.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/item/Item", "registerItemBlock", "(Lnet/minecraft/block/Block;)V"));
				
				//inject
				mn0.instructions.insert(ns);
			}
			
			//end
			break;
		}
		
		//null表示没修改
		return cn == null ? null : SC$CodeRuler.write(cn);
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
