package io.github.mapsegundo.imageliteapi.application.images;

import io.github.mapsegundo.imageliteapi.domain.entity.Image;
import io.github.mapsegundo.imageliteapi.domain.enums.ImageExtension;
import io.github.mapsegundo.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe controladora para operações relacionadas a imagens.
 */
@RestController
@RequestMapping("/v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService service;

    private final ImageMapper mapper;

    /**
     * Salva uma nova imagem no sistema.
     *
     * @param file O arquivo de imagem a ser salvo.
     * @param name O nome da imagem.
     * @param tags Uma lista de tags associadas à imagem.
     * @return ResponseEntity com o status de criação e a URI da imagem salva.
     * @throws IOException Se houver um erro ao ler o arquivo.
     */
    @PostMapping
    public ResponseEntity save(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("tags") List<String> tags
    ) throws IOException {
        log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

        Image image = mapper.mapToImage(file, name, tags);
        Image savedImage = service.save(image);
        URI imageUri = buildImageUrl(savedImage);

        return ResponseEntity.created(imageUri).build();
    }

    /**
     * Recupera uma imagem pelo seu ID.
     *
     * @param id O ID da imagem a ser recuperada.
     * @return ResponseEntity contendo os dados da imagem e cabeçalhos apropriados se encontrada, ou um status de não encontrado.
     */
    @GetMapping("{id}")
    private ResponseEntity<byte[]> getImage(@PathVariable String id) {
        var possibleImage = service.getById(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() + "\"", image.getFileName());

        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

    /**
     * Pesquisa imagens com base na extensão e/ou consulta.
     *
     * @param extension A extensão do arquivo para filtrar imagens (opcional).
     * @param query A consulta de pesquisa para filtrar imagens (opcional).
     * @return ResponseEntity contendo uma lista de objetos ImageDTO que correspondem aos critérios de pesquisa.
     */
    @GetMapping
    public ResponseEntity<List<ImageDTO>> search(
            @RequestParam(value = "extension", required = false, defaultValue = "") String extension,
            @RequestParam(value = "query", required = false) String query) {

        var result = service.search(ImageExtension.ofName(extension), query);

        var images = result.stream().map(image -> {
            var url = buildImageUrl(image);
            return mapper.imageToDTO(image, url.toString());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    /**
     * Constrói a URL para acessar uma imagem.
     *
     * @param image O objeto Image para o qual construir a URL.
     * @return URI representando a URL para acessar a imagem.
     */
    private URI buildImageUrl(Image image) {
        String imagePath = "/" + image.getId();
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(imagePath)
                .build()
                .toUri();
    }
}
