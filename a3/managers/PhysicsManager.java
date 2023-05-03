package a3.managers;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsEngineFactory;
import tage.physics.PhysicsObject;

public class PhysicsManager {
    private PhysicsEngine physicsEngine;

    public PhysicsManager() {
        String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
        float[] gravity = { 0f, 0f, 0f };
        physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEngine.initSystem();
        physicsEngine.setGravity(gravity);

    }

    public void update(float elapsTime) {
        physicsEngine.update(elapsTime);
    }

    public PhysicsObject addBoxObject(float mass, double[] transform, float[] size) {
        PhysicsObject po = physicsEngine.addBoxObject(physicsEngine.nextUID(), mass, transform, size);
        po.setBounciness(0.0f);
        return po;
    }

    public PhysicsObject addCapsuleObject(float mass, double[] transform, float radius, float height) {
        PhysicsObject po = physicsEngine.addCapsuleObject(physicsEngine.nextUID(), mass, transform, radius, height);

        return po;
    }

    public PhysicsObject addStaticPlaneObject(double[] transform, float[] up_vector, float plane_constant) {
        PhysicsObject po = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), transform, up_vector, plane_constant);
        po.setBounciness(0.0f);
        return po;
    }

    public int nextUID() {
        return physicsEngine.nextUID();
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

}
