package waes.assesment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.enums.DataType;
import waes.assesment.services.DiffService;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(value = BinaryDiffController.URI_PREFIX)
@RestController
public class BinaryDiffController {
    public static final String URI_PREFIX = "/v1/diff";

    private final DiffService diffService;

    public BinaryDiffController(DiffService diffService) {
        this.diffService = diffService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{diffId}/left")
    public void createLeftBinaryData(@PathVariable final UUID diffId, @RequestBody @Valid DiffDataDTO diffDataDTO){
        diffDataDTO.setDataType(DataType.LEFT);
        this.diffService.create(diffId, diffDataDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{diffId}/right")
    public void createRightBinaryData(@PathVariable final UUID diffId, @RequestBody @Valid DiffDataDTO diffDataDTO){
        diffDataDTO.setDataType(DataType.RIGHT);
        this.diffService.create(diffId, diffDataDTO);
    }

    @GetMapping("/{diffId}")
    public ResponseEntity<?> compare(@PathVariable final UUID diffId){
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
