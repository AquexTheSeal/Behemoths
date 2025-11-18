package org.celestialworkshop.behemoths.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class BMHierarchicalModel<E extends Entity> extends HierarchicalModel<E> {

    public abstract List<ModelPart> parts();

}
