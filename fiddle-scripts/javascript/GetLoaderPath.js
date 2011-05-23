importPackage(net.java.dev.fiddle);

clazz = new FiddleServlet().getClass();
clazz.getProtectionDomain().getCodeSource().getLocation();


// or

clazz = request.getClass();
clazz.getProtectionDomain().getCodeSource().getLocation();