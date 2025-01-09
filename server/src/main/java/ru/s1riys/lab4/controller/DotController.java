package ru.s1riys.lab4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.s1riys.lab4.annotation.HasPermissions;
import ru.s1riys.lab4.domain.dto.CreateDotRequest;
import ru.s1riys.lab4.domain.dto.DeleteResponse;
import ru.s1riys.lab4.domain.dto.DotResponse;
import ru.s1riys.lab4.domain.entity.Dot;
import ru.s1riys.lab4.domain.entity.Permission;
import ru.s1riys.lab4.domain.entity.User;
import ru.s1riys.lab4.domain.mapper.DotMapper;
import ru.s1riys.lab4.service.DotService;
import ru.s1riys.lab4.service.UserService;

@RestController
@RequestMapping("/dots")
public class DotController {
    @Autowired
    private DotService dotService;
    @Autowired
    private UserService userService;
    @Autowired
    private DotMapper dotMapper;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @HasPermissions({ Permission.DOT_CREATE })
    public DotResponse createDot(@RequestBody @Valid CreateDotRequest dto) {
        User currentUser = userService.getCurrentUser();
        Dot result = dotService.createDot(dto, currentUser);
        return dotMapper.toResponse(result);
    }

    @GetMapping("/my")
    // @HasPermissions({ Permission.DOT_READ })
    public List<DotResponse> getMyDots() {
        User currentUser = userService.getCurrentUser();
        return currentUser.getDots().stream().map(dotMapper::toResponse).toList();
    }

    @DeleteMapping("/my")
    @HasPermissions({ Permission.DOT_DELETE + "random-string" })
    public DeleteResponse deleteMyDots() {
        User currentUser = userService.getCurrentUser();
        dotService.deleteAllByOwner(currentUser);
        return new DeleteResponse("ok");
    }
}
