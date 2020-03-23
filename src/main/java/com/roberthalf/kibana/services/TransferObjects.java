package com.roberthalf.kibana.services;

import com.roberthalf.kibana.configuration.KibanaConfiguration;
import com.roberthalf.kibana.dao.ExportSpec;
import com.roberthalf.kibana.dao.KibanaObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferObjects {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KibanaConfiguration config;

    public void transferIndexPattern() throws Exception {

        //export from prod
        String response = postService(config.getProdUrl(), KibanaObject.INDEX_PATTERN);
        log.debug(response);
        //import into dev
        response = postService(config.getDevUrl(), response);
        log.debug(response);

    }

    private String postService(String url, String body) throws Exception{

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> _body = new LinkedMultiValueMap<>();

        //dziala
        File tempFile = File.createTempFile("index-pattern", ".ndjson");
        log.debug(tempFile.getAbsolutePath());
        try (FileWriter fw = new FileWriter(tempFile)){
            fw.write(body);
            fw.close();
            FileSystemResource fsr = new FileSystemResource(tempFile);
            _body.add("file", fsr);
        }

        //test2
        /*Path path = Paths.get("index-pattern.ndjson");
        byte[] strToBytes = body.getBytes();
        Files.write(path, strToBytes);
        log.info("filepath:"+path.getParent());
        FileSystemResource fsr2 = new FileSystemResource(path);
        _body.add("file", fsr2);
         */

        headers.add("kbn-xsrf", "true");
        //HttpEntity<String> requestEntity = new HttpEntity<?>(_body, headers);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(_body, headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("overwrite", "true");

        return postService(builder.build().encode().toString(), requestEntity);
    }


    private String postService(String url, KibanaObject object) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("kbn-xsrf", "true");
        ExportSpec body = new ExportSpec();
        body.setIncludeReferencesDeep(true);
        body.setType(Arrays.asList(object.getValue()));
        HttpEntity<ExportSpec> requestEntity = new HttpEntity<>(body, headers);

        return postService(url, requestEntity);
    }

    private String postService(String url, HttpEntity<?> requestEntity) {

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("kbn-xsrf", "true");
        //HttpEntity<ExportSpec> requestEntity = new HttpEntity<>(body, headers);
        //requestEntity.getHeaders().

        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode() != HttpStatus.OK)
            log.error(String.format("Error code %d while calling service %s", response.getStatusCode(), url));
        else
            //log.info(String.format("Export of %s ended successfully", body.getType().get(0)));
            log.info(String.format("Rest call ended successfully"));

        log.debug(response.getBody());
        return response.getBody();

    }

}
