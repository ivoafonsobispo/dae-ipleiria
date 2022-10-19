<template>
  <!-- easy components usage, already shipped with bootstrap css-->
  <div>
    <b-container>
      <!-- try to remove :fields=”fields” to see the magic -->
      <b-table striped over :items="students" :fields="fields">
        <template v-slot:cell(actions)="row">
          <nuxt-link class="btn btn-link" :to="`/students/${row.item.username}`"
            >Details</nuxt-link
          >
        </template>
      </b-table>
      <nuxt-link to="/create">Create a New Student</nuxt-link>
      <br />
      <nuxt-link to="/">Back</nuxt-link>
    </b-container>
  </div>
</template>
<script>
export default {
  data() {
    return {
      fields: ["username", "name", "email", "courseName", "actions"],
      students: [],
    };
  },
  created() {
    this.$axios.$get("/api/students/all").then((students) => {
      this.students = students;
    });
  },
};
</script>
<style></style>
