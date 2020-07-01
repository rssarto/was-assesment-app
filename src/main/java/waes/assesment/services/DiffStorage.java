package waes.assesment.services;

import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

public interface DiffStorage {
    void create(DiffContent diffContent);
    DiffDataEntity findById(UUID diffDataId);
}
