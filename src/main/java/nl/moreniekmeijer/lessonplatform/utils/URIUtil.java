package nl.moreniekmeijer.lessonplatform.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIUtil {

    public static URI createResourceUri(Long resourceId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

    public static URI createResourceUriUser(String username) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(username)
                .toUri();
    }

    public static URI createFileAssignmentUri(Long materialId) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/materials/{id}/file")
                .buildAndExpand(materialId)
                .toUri();
    }

    public static URI createLinkAssignmentUri(Long materialId) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/materials/{id}/link")
                .buildAndExpand(materialId)
                .toUri();
    }
}

