package pl.alburnus.multitenancy.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.alburnus.multitenancy.context.CurrentTenantContext;
import pl.alburnus.multitenancy.domain.Team;
import pl.alburnus.multitenancy.domain.TeamRepository;

import java.util.UUID;

@RestController
@RequestMapping("/team")
public class TeamResource {

    @Autowired
    private TeamRepository teamRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Team> createTeam(@RequestHeader("X-TenantID") String tenantName) {
        CurrentTenantContext.setThreadTenantContext(tenantName);

        Team team = new Team();
        team.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        team.setName(tenantName);
        teamRepository.save(team);

        return ResponseEntity.ok(team);
    }
}
