package waes.assesment.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import waes.assesment.controllers.BinaryDiffController;
import waes.assesment.controllers.CustomResponseEntityExceptionHandler;
import waes.assesment.exceptions.DiffDataNotExistsException;
import waes.assesment.exceptions.RecordNotFoundException;
import waes.assesment.repositories.DiffDataRepository;
import waes.assesment.resources.dto.ChangeLog;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.dto.DiffResultDTO;
import waes.assesment.resources.enums.DataType;
import waes.assesment.services.DiffService;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DisplayName("[UnitTest] BinaryDiffController")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = BinaryDiffController.class)
    public class BinaryDiffControllerTest {

    public static final String BASE64_WAES_LOGO = "data:image/png;base64,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";
    public static final String BASE64_WAES_LOGO_V2 = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxERDhUQEA8TEhIXEBETGBEXGBAQERkWGBEWGRkXFRcYHCggGRoxHBYXITEtJSo3LjouFx8zPT8sOCgtLisBCgoKDg0OGRAQGzcgICYvLTctNysrLTcvNysrNy0wNzU1NystKzAuNzUyKy0uLS42Kzc1LSstNy0rKy4tLy0tOP/AABEIAMgAyAMBIgACEQEDEQH/xAAcAAEAAgMBAQEAAAAAAAAAAAAABwgEBQYDAQL/xABKEAABAwICBgYFCAYHCQAAAAABAAIDBBEFEgYHEyExUSJBYXGBkRQXQpPTCDJSVYKSocFDVGJjg6MWIyRyotHwFTNTZHOxs8LS/8QAGgEBAAIDAQAAAAAAAAAAAAAAAAMFAQIEBv/EACgRAQACAgECBQMFAAAAAAAAAAABAgMRBBIxBSEiQVETYaEUMoHh8P/aAAwDAQACEQMRAD8Ag1ERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERARdpqkwimrMXjpquLaxOjm6GZ7Ok1hcDdhB6ip59UuCfV497VfEQVSRWkxHVTgzYJHNoAHCJ5B2tUbENNv0iq2gIp51T6sqKowtlTX021kle57LvmjyxXyt3McL3sXdzgu09UuCfV497VfEQVSRd5rl0epqDExDSxbKJ1NHJkzPfvL3tJu8k+ysbVRowzEcUZDKzNAxj5ZW3c27Wiwbdpvvc5vhdBxiK1vqlwT6vHvar4ieqXBPq8e9qviIKpIul0opIJ8Xkp8MgyxGcQQxtc9+YghubM8k2LrnuIU94PqewqOmjZUUu3mDBtJdpUMzP8AaIDXgAX4buCCsC+K1nqmwT6vHvav4i0mmmrPCIMMqpoaMMljpppGP2tSbOawkbnPsUNq3IsvC6Mz1EUDeMk0cY73vDfzVofVLgn1ePe1fxEFU0VrfVLgn1ePe1XxE9UuCfV497VfEQVSRWt9UuCfV497VfEUD6yMDhjxx9Dh1OQBsY2xNMkhdI6NrjbMSb3dbwQcUin7Q3UfCxjZcTeZZCAfR2OL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BSlERB9VzNE5tphtLJ9KjpnecLT+apmrAaK60qGHC6amNU2KaOnjjeXx1Lw0taBuEbDm4cwsxG2trajaWaytjhbnleGN5n8ua4fSHTwtY7YDIxrXEyuHSsBc5Wnh4rR/0wwGR+0qsWkndy2NWxncAI93gVo9ZmnWGPww0uFva6SV7WyOEc0REQ6R6UjQSSQ0dxK6KThp39U/hwZKcrNOonor+f6RFi9e+oqJJ5HFz3vLiSSTvPMqXtEtSvpFPFU1NaQyWKOURxNBdlewOF3v4Gx+iVCqsjofrRwmHDaWGesySx0sEb27KqdZzI2tIu2Mg8Opc+1h0xrToME1aYVS2LaRsrx7c39cfJ3RB7gms/EvRMHmETbPkApo2tAHSl6JDbcDlzHwWP63ME/X/wCTWfDXG6c6f4ZWTwCOrBiizvuY6ho2juiCQY79FoPV+k7Ftjr1WiJnSLPeceObVjc/ENVgGGCmp2x8XcXHm48f8vBdTgujk9SQQMkf/Edw+yOtY2C6V6PxWdNXiV/Iw1mzHcNnv8fwXQz63cFbG5zKwvcGuLYxFVNLiBuaC6OwJ4b9yssnOrSvRij+VFg8Hvlv9Xkz39myoxhdA8skq6Zs4AzbWWFkouL/ADXHoixWy/pdhv1lR+/p/wD6VRsaxOSqqZamY3klkc93K5PAdgG4dgWCqy17Wndp2v8AFipir00jULR6wq8OfHE03AbtCRvBzbm28L+a49ao6Z0UzmmSpDOhG0ksnIAaxrd+Vh5dS6bCNK9HYbOlr9s/tgq8g7m7P/urfHyMWDFEb3P2eXzcHkcvk2trUb7z8I+pNAa/EK2UwQlsW2f/AF8nQhF3dR4u+yCpY0R1RUNJaSo/tkw33eAIQf2Y+B+1fwWa3W1gYFhX/wAmr+GvvrcwT9f/AJNZ8NVNrbmZeox4+msRPm7ZjQAAAAALADcLcgv2uG9buCfr/wDJrPhp63cE/X/5NZ8NaJHcooF1sazopfRThNa/PHLJI97WzRWOVoYCJGjOCC/dvHNZ2ievVhAjxKAtduHpEIzNPa+Mm4+yT3BBNi+ALmsO0/wqcAx4jTi/U94gd92SxWVU6YYbGLvxGlH8aEnwANyg3ijzXVpSyjwx8DXDb1LXRNb1iMi0jz2ZTbvcORWs0r13UUDS2haaqW255Do4Ae0mzndwHioEx/G6iuqHVNVIZJHdfBoHU1o9lo5INaiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiD//2Q==";
    public static final String LEFT_API_SUFFIX_TEMPLATE = "%s/%s/left";
    public static final String RIGHT_API_SUFFIX_TEMPLATE = "%s/%s/right";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiffService diffService;

    @MockBean
    private DiffDataRepository diffDataRepository;

    @Test
    @DisplayName("Create left data")
    public void postLeftBinaryData() throws Exception {
        final DiffDataDTO diffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO, DataType.LEFT);
        final UUID diffId = UUID.randomUUID();
        performCreateRequest(diffDataDTO, diffId, LEFT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create right data")
    public void postRightBinaryData() throws Exception {
        final UUID diffId = UUID.randomUUID();
        final DiffDataDTO diffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO_V2, DataType.RIGHT);
        performCreateRequest(diffDataDTO, diffId, RIGHT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Create data with no content should throw exception")
    public void postLeftWithNoContent_shouldReturnBadRequest() throws Exception {
        final UUID diffId = UUID.randomUUID();
        final DiffDataDTO diffDataDTO = new DiffDataDTO("", DataType.RIGHT);
        performCreateRequest(diffDataDTO, diffId, RIGHT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(CustomResponseEntityExceptionHandler.VALIDATION_ERROR))
                .andExpect(jsonPath("$.subErrors.length()").value(1))
                .andExpect(jsonPath("$.subErrors[0].object").value("diffDataDTO"))
                .andExpect(jsonPath("$.subErrors[0].field").value("data"))
                .andExpect(jsonPath("$.subErrors[0].message").value(DiffDataDTO.DATA_VALIDATION_MESSAGE));
    }

    @Test
    @DisplayName("Compare data with nto existing id")
    public void compareDataUsingNotExistingId_shouldReturnNotFound() throws Exception {
        final UUID diffDataId = UUID.randomUUID();
        when(this.diffService.compare(diffDataId)).thenThrow(new RecordNotFoundException(String.format(RecordNotFoundException.MESSAGE_TEMPLATE, diffDataId)));
        this.performCompareRequest(diffDataId)
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.debugMessage").value(String.format(RecordNotFoundException.MESSAGE_TEMPLATE, diffDataId)));
    }

    @Test
    @DisplayName("Compare data with equal content")
    public void compareDataWithEqualContent() throws Exception {
        final UUID diffDataId = UUID.randomUUID();
        when(this.diffService.compare(diffDataId)).then(invocationOnMock -> {
            final Map<DataType, DiffDataDTO> diffMap = new HashMap<>();
            diffMap.put(DataType.LEFT, new DiffDataDTO(BASE64_WAES_LOGO, DataType.LEFT));
            diffMap.put(DataType.RIGHT, new DiffDataDTO(BASE64_WAES_LOGO, DataType.RIGHT));
            DiffResultDTO diffResultDTO = new DiffResultDTO(diffMap);
            diffResultDTO.setEqual(true);
            return diffResultDTO;
        });

        final DiffDataDTO leftDiffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO, DataType.LEFT);
        this.performCreateRequest(leftDiffDataDTO, diffDataId, LEFT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        final DiffDataDTO rightDiffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO, DataType.RIGHT);
        this.performCreateRequest(rightDiffDataDTO, diffDataId, RIGHT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        this.performCompareRequest(diffDataId)
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.diffMap").exists())
                .andExpect(jsonPath("$.dataSizeMap").doesNotExist())
                .andExpect(jsonPath("$.changeLogList").doesNotExist())
                .andExpect(jsonPath("$.equal").value(true));
    }

    @Test
    @DisplayName("Compare data with not equal data")
    public void compareDataWithNotEqualContent() throws Exception {
        final UUID diffDataId = UUID.randomUUID();
        when(this.diffService.compare(diffDataId)).then(invocationOnMock -> {
            final Map<DataType, DiffDataDTO> diffMap = new HashMap<>();
            diffMap.put(DataType.LEFT, new DiffDataDTO("xpto", DataType.LEFT));
            diffMap.put(DataType.RIGHT, new DiffDataDTO("xtto", DataType.RIGHT));
            DiffResultDTO diffResultDTO = new DiffResultDTO(diffMap);
            diffResultDTO.setEqual(false);

            final List<ChangeLog> changeLogList = new ArrayList<>();
            changeLogList.add(new ChangeLog(1, 1));
            diffResultDTO.setChangeLogList(changeLogList);
            return diffResultDTO;
        });

        final DiffDataDTO leftDiffDataDTO = new DiffDataDTO("xpto", DataType.LEFT);
        this.performCreateRequest(leftDiffDataDTO, diffDataId, LEFT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        final DiffDataDTO rightDiffDataDTO = new DiffDataDTO("xtto", DataType.RIGHT);
        this.performCreateRequest(rightDiffDataDTO, diffDataId, RIGHT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        this.performCompareRequest(diffDataId)
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.diffMap").exists())
                .andExpect(jsonPath("$.dataSizeMap").doesNotExist())
                .andExpect(jsonPath("$.changeLogList").exists())
                .andExpect(jsonPath("$.equal").value(false));
    }

    @Test
    @DisplayName("Compare data with not equal size")
    public void compareDataWithNotEqualSize() throws Exception {
        final UUID diffDataId = UUID.randomUUID();
        when(this.diffService.compare(diffDataId)).then(invocationOnMock -> {
            final Map<DataType, DiffDataDTO> diffMap = new HashMap<>();
            diffMap.put(DataType.LEFT, new DiffDataDTO("xpto", DataType.LEFT));
            diffMap.put(DataType.RIGHT, new DiffDataDTO("xptoz", DataType.RIGHT));
            DiffResultDTO diffResultDTO = new DiffResultDTO(diffMap);
            diffResultDTO.setEqual(false);

            final Map<DataType, Integer> dataSizeMap = new HashMap<>();
            dataSizeMap.put(DataType.LEFT, diffResultDTO.getDiffMap().get(DataType.LEFT).getData().length());
            dataSizeMap.put(DataType.RIGHT, diffResultDTO.getDiffMap().get(DataType.RIGHT).getData().length());
            diffResultDTO.setDataSizeMap(dataSizeMap);
            return diffResultDTO;
        });

        final DiffDataDTO leftDiffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO, DataType.LEFT);
        this.performCreateRequest(leftDiffDataDTO, diffDataId, LEFT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        final DiffDataDTO rightDiffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO_V2, DataType.RIGHT);
        this.performCreateRequest(rightDiffDataDTO, diffDataId, RIGHT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        this.performCompareRequest(diffDataId)
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.diffMap").exists())
                .andExpect(jsonPath("$.dataSizeMap").exists())
                .andExpect(jsonPath("$.changeLogList").doesNotExist())
                .andExpect(jsonPath("$.equal").value(false));
    }

    @Test
    @DisplayName("Compare data with not complete pair should return an exception")
    public void compareIncompletePairData_shouldReturnException() throws Exception {
        final UUID diffDataId = UUID.randomUUID();
        when(this.diffService.compare(diffDataId)).thenThrow(new DiffDataNotExistsException(DataType.RIGHT));

        final DiffDataDTO leftDiffDataDTO = new DiffDataDTO(BASE64_WAES_LOGO, DataType.LEFT);
        this.performCreateRequest(leftDiffDataDTO, diffDataId, LEFT_API_SUFFIX_TEMPLATE)
                .andExpect(status().isCreated());

        this.performCompareRequest(diffDataId)
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> {
                    log.info("Api response: {}", mvcResult.getResponse().getContentAsString());
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).isNotBlank();
                })
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.debugMessage").value(String.format(DiffDataNotExistsException.DIFF_DATA_MESSAGE_TEMPLATE, DataType.RIGHT)));
    }

    private ResultActions performCompareRequest(final UUID diffDataId) throws Exception {
        return this.mockMvc.perform(get(String.format("%s/%s", BinaryDiffController.URI_PREFIX, diffDataId.toString())));
    }

    private ResultActions performCreateRequest(DiffDataDTO diffDataDTO, UUID diffId, String s) throws Exception {
        return this.mockMvc.perform(post(String.format(s, BinaryDiffController.URI_PREFIX, diffId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(diffDataDTO)));
    }

}