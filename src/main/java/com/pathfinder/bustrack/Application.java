package com.pathfinder.bustrack;

import com.pathfinder.bustrack.model.Vehicle;
import com.pathfinder.bustrack.model.VehicleDao;
import org.slf4j.LoggerFactory;
import spark.*;
import spark.template.velocity.*;

import java.util.*;

import static spark.Spark.*;

public class Application {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");


        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        staticFiles.location("/public");
        port(9999);

        get("/", (req, res) -> {
            log.warn("Rendering Vehicles...");
            return renderVehicles(req);
        });

        get("/map", (req, res) -> {
            log.warn("Rendering Map...");
            return renderMap(req);
        });

        get("/vehicles/:chassisnumber/edit", (req, res) -> {
            log.warn("Rendering edit...");
            return renderEditVehicle(req);
        });

        post("/vehicles", (ICRoute) (req) -> {
            log.warn("Accepted Post...");
            //VehicleDao.add(new Vehicle(req.queryParams("vehicle-chassisnumber"), req.queryParams("vehicle-make"), req.queryParams("vehicle-model"), req.queryParams("vehicle-colour"), Integer.parseInt(req.queryParams("vehicle-capacity"))));
            VehicleDao.add(new Vehicle(req.queryParams("vehicle-chassisnumber")));
        });

        //delete("/vehicles/completed",      (ICRoute) (req) -> VehicleDao.removeCompleted());
        delete("/vehicles/:chassisnumber", (ICRoute) (req) -> VehicleDao.remove(req.params("chassisnumber")));
//        put("/vehicles/toggle_status",     (ICRoute) (req) -> VehicleDao.toggleAll(req.queryParams("toggle-all") != null));
        put("/vehicles/:chassisnumber",               (ICRoute) (req) -> {
            log.warn("Accepted PUT...");
            VehicleDao.update(new Vehicle(req.queryParams("vehicle-chassisnumber"), req.queryParams("vehicle-make"), req.queryParams("vehicle-model"), req.queryParams("vehicle-colour"), Integer.parseInt(req.queryParams("vehicle-capacity"))));
        });
        //put("/vehicles/:chassisnumber",               (ICRoute) (req) -> VehicleDao.update(req.params("chassisnumber"), req.queryParams("todo-title")));
//        put("/vehicles/:chassisnumber/toggle_status", (ICRoute) (req) -> VehicleDao.toggleStatus(req.params("chassisnumber")));

        after((req, res) -> {
            if (res.body() == null) { // if the route didn't return anything
                res.body(renderVehicles(req));
            }
        });
    }

    private static String renderVehicles(Request req) {
        String statusStr = req.queryParams("status");
        Map<String, Object> model = new HashMap<>();
        //model.put("todos", VehicleDao.ofStatus(statusStr));
        model.put("vehicles", VehicleDao.all());
        model.put("filter", Optional.ofNullable(statusStr).orElse(""));
//        model.put("activeCount", VehicleDao.ofStatus(Status.ACTIVE).size());
//        model.put("anyCompleteTodos", VehicleDao.ofStatus(Status.COMPLETE).size() > 0);
//        model.put("allComplete", VehicleDao.all().size() == VehicleDao.ofStatus(Status.COMPLETE).size());
        //model.put("status", Optional.ofNullable(statusStr).orElse(""));
        if ("true".equals(req.queryParams("ic-request"))) {
            return renderTemplate("velocity/vehicleList.vm", model);
        }
        return renderTemplate("velocity/index.vm", model);
    }

    private static String renderMap(Request req) {
        Map<String, Object> model = new HashMap<>();
        return renderTemplate("velocity/map.vm", model);
    }

    private static String renderEditVehicle(Request req) {
        return renderTemplate("velocity/editVehicle.vm", new HashMap() {{
            put("vehicle", VehicleDao.find(req.params("chassisnumber")));
        }});
    }


    private static String renderTemplate(String template, Map model) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, template));
    }

    //TODO: What is this? - intercooler route - intercooler is a javascript helper.
    @FunctionalInterface
    private interface ICRoute extends Route {
        default Object handle(Request request, Response response) throws Exception {
            handle(request);
            return "";
        }

        void handle(Request request) throws Exception;
    }
}
