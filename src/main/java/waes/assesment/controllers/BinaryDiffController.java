package waes.assesment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.services.DiffService;

import java.util.UUID;

@RequestMapping(value = BinaryDiffController.URI_PREFIX)
@RestController
public class BinaryDiffController {
    public static final String URI_PREFIX = "/v1/diff";

    private final DiffService diffService;

    public BinaryDiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @PostMapping("/{diffId}/left")
    public ResponseEntity<?> createLeftBinaryData(@PathVariable final UUID diffId, @RequestBody DiffDataDTO diffDataDTO){
        this.diffService.create(diffDataDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/{diffId}/right")
    public ResponseEntity<?> createRightBinaryData(@PathVariable final UUID diffId, @RequestBody DiffDataDTO diffDataDTO){
        this.diffService.create(diffDataDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
