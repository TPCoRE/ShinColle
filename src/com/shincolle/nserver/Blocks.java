package com.shincolle.nserver;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Blocks for shincolle
 * */
public final class Blocks {
	
	public static final Block POLYMETAL = new Block(Material.SAND, MapColor.GRAY).setCreativeTab(CreativeTabs.MATERIALS).setUnlocalizedName("polymetal");
	public static final Block ABYSSMETAL = new Block(new MaterialTransparent(MapColor.RED), MapColor.RED) {
		
		@Override
		public final boolean isOpaqueCube(IBlockState state) {
			return false;
		}
		
		@Override
		public final boolean isFullCube(IBlockState state) {
	        return false;
	    }
		
		@Override
	    public final AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
	        return NULL_AABB;
	    }
	}.setCreativeTab(CreativeTabs.MATERIALS).setUnlocalizedName("abyssmetal");
}
