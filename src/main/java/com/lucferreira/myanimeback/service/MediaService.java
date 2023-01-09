package com.lucferreira.myanimeback.service;
import com.lucferreira.myanimeback.model.media.Media;
import com.lucferreira.myanimeback.model.media.MediaDto;
import com.lucferreira.myanimeback.model.media.MediaForm;
import com.lucferreira.myanimeback.repository.MediaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    public List<MediaDto> listMediaByType(String type){
        String realType = getRealType(type);
        List<Media> medias = mediaRepository.findAllByType(realType);
        return medias.stream()
                .map(Media::toDto)
                .collect(Collectors.toList());
    }

    public MediaDto getMedia(String type,Long id)  {
        String realType = getRealType(type);
        Optional<Media> optionalMedia = mediaRepository.findByTypeAndId(realType,id);
        if(optionalMedia.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("%s with id %d not found",realType,id));
        }
        return Media.toDto(optionalMedia.get());
    }

    public MediaDto updateMedia(MediaForm mediaForm, Long id){

        Media toUpdateMedia = Media.fromForm(mediaForm,id);
        Media updatedMedia = saveMedia(toUpdateMedia);

        return Media.toDto(updatedMedia);
    }

    public MediaDto deleteMedia(String type, Long id){
        String realType = getRealType(type);
        Optional<Media> optionalMedia = mediaRepository.findByTypeAndId(realType,id);
        if(optionalMedia.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("%s with id %d not found",realType,id));
        }
        Media media = optionalMedia.get();
        deleteMedia(media);
        return Media.toDto(media);
    }

    public MediaDto createMedia(MediaForm mediaForm) {
        System.out.println(mediaForm.getName());
        Media newMedia = Media.fromForm(mediaForm);
        Media createdMedia = saveMedia(newMedia);
        return Media.toDto(createdMedia);
    }
    private Media saveMedia(Media toSaveMedia){
        try {
            return mediaRepository.save(toSaveMedia);
        } catch (DataIntegrityViolationException  e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteMedia(Media media){
        try {
         mediaRepository.delete(media);
        } catch (DataIntegrityViolationException  e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String getRealType(String mediaType){
        if (mediaType.equals("mangas") || mediaType.equals("manga")) {
            return "manga";
        }
        return "anime";
    }
}
