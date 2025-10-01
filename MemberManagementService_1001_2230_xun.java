// 代码生成时间: 2025-10-01 22:30:00
package com.example.member;

import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/members")
@ApplicationScoped
@RegisterForReflection
public class MemberManagementService {

    private static final List<Member> members = new ArrayList<>();

    // Simulate a database with in-memory list
    public static class Member {
        private final UUID id;
        private String name;
        private String email;

        public Member(String name, String email) {
            this.id = UUID.randomUUID();
            this.name = name;
            this.email = email;
        }

        // Getters and setters
        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> listMembers() {
        return members;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMember(Member member) {
        if (member.getName() == null || member.getEmail() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name and email are required").build();
        }

        members.add(member);
        return Response.status(Response.Status.CREATED).entity(member).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMember(@PathParam("id") UUID id, Member member) {
        Member existingMember = members.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (existingMember == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Member not found").build();
        }

        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        return Response.status(Response.Status.OK).entity(existingMember).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteMember(@PathParam("id\) UUID id) {
        Member existingMember = members.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (existingMember == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Member not found").build();
        }

        members.remove(existingMember);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
