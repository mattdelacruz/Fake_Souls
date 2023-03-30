var JavaPackages = new JavaImporter(
    Packages.tage.Light,
    Packages.org.joml.Vector3f
);

with (JavaPackages) {
    var light = new Light();
    Light.setGlobalAmbient(0.5, 0.5, 0.5);
	light.setLocation(new Vector3f(5.0, 4.0, 2.0));
}