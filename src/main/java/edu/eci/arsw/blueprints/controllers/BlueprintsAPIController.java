package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.dto.ApiResponse;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) {
        this.services = services;
    }

    @Operation(
            summary = "Get all blueprints",
            description = "Returns all registered blueprints."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Blueprints retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Set<Blueprint>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "execute ok", services.getAllBlueprints())
        );
    }

    @Operation(
            summary = "Get blueprints by author",
            description = "Returns all blueprints created by a specific author."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Blueprints retrieved successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Author not found"
    )
    @GetMapping("/{author}")
    public ResponseEntity<?> byAuthor(@PathVariable String author) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "execute ok", services.getBlueprintsByAuthor(author))
            );
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "blueprints with author " + author + " not found", null));
        }
    }

    @Operation(
            summary = "Get blueprint by author and name",
            description = "Returns one blueprint using author and blueprint name."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Blueprint found"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Blueprint not found"
    )
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> byAuthorAndName(
            @PathVariable String author,
            @PathVariable String bpname
    ) {
        try {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "execute ok", services.getBlueprint(author, bpname))
            );
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "blueprint with author " + author + " and name " + bpname + " not found", null));
        }
    }

    @Operation(
            summary = "Create a new blueprint",
            description = "Creates a new blueprint with author, name and points."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Blueprint created successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request or blueprint already exists"
    )
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "blueprint created", null));

        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "blueprint already exists", null));
        }
    }

    @Operation(
            summary = "Add point to blueprint",
            description = "Adds a new point to an existing blueprint."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "202",
            description = "Point added successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Blueprint not found"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid point data"
    )
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(
            @PathVariable String author,
            @PathVariable String bpname,
            @RequestBody Point p
    ) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(202, "point added", null));

        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "blueprint with author " + author + " and name " + bpname + " not found", null));
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) {
    }
    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponse<Blueprint>> updateBlueprint(
            @PathVariable String author,
            @PathVariable String bpname,
            @Valid @RequestBody NewBlueprintRequest req
    ) {
        try {
            Blueprint bp = new Blueprint(author, bpname, req.points());

            services.updateBlueprint(author, bpname, bp);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>(202, "blueprint updated", bp));

        } catch (BlueprintNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "blueprint not found", null));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "blueprint already exists", null));
        }
    }
}