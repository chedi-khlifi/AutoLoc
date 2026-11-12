package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule)")
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    @Operation(summary = "Lister les paiements")
    @ApiResponse(responseCode = "200", description = "Liste retournée (éventuellement vide)")
    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @Operation(summary = "Consulter par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultation réussie"),
            @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    })
    @GetMapping("/{id}")
    public Paiement findById(@Parameter(description = "Identifiant") @PathVariable Long id) {
        return paiementService.findById(id);
    }
}
