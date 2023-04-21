package a3;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsEngineFactory;
import tage.physics.PhysicsObject;

public class PhysicsManager {
    private PhysicsEngine physicsEngine;

    public PhysicsManager() {
        String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
        float[] gravity = { 0f, -10f, 0f };
        physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEngine.initSystem();
        physicsEngine.setGravity(gravity);

    }

    public void update(float elapsTime) {
        physicsEngine.update(elapsTime);
    }

    public PhysicsObject addBoxObject(int uid, float mass, double[] transform, float[] size) {
        PhysicsObject po = physicsEngine.addBoxObject(uid, mass, transform, size);
        po.setBounciness(0.0f);
        return po;
    }

    public PhysicsObject addStaticPlaneObject(int uid, double[] transform, float[] up_vector, float plane_constant) {
        PhysicsObject po = physicsEngine.addStaticPlaneObject(uid, transform, up_vector, plane_constant);
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
