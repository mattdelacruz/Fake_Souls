package a3;

import tage.shapes.*;
import tage.*;

public class ManualBurger extends ManualObject {
        private float[] vertices = new float[] {
                        -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, // front
                        1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, // right
                        1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, // back
                        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, // left
                        -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, // LF
                        1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, // RR
                        -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 0.0f, -3.0f, 0.0f, // LB
                        1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 0.0f, -3.0f, 0.0f, // FB
                        1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 0.0f, -3.0f, 0.0f, // RB
                        -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, -3.0f, 0.0f// RR
        };
        private float[] texcoords = new float[] {
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,
                        0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 1.0f,

                        0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                        1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f,
                        1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f,
                        1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f,
                        1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -1.0f,
        };
        private float[] normals = new float[] {
                        0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,

                        0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                        0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
                        -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f
        };

        public ManualBurger() {
                super();
                setNumVertices(30);
                setVertices(vertices);
                setTexCoords(texcoords);
                setNormals(normals);
                setAnimated(true);
                setMatAmb(Utils.goldAmbient());
                setMatDif(Utils.goldDiffuse());
                setMatSpe(Utils.goldSpecular());
                setMatShi(Utils.goldShininess());
        }
}
