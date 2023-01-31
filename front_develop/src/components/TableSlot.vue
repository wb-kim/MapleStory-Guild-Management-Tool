<template>
  <div class="table-responsive">
    <table class="table table-bordered">
      <slot></slot>
    </table>
  </div>
</template>

<script>
export default {
  name: "table-slot",
};
</script>

<style scoped>
div {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  padding: 2rem;
  margin: 2rem auto;
  background-color: white;
  overflow-x: auto;
  text-align: center;
  table-layout: auto;
}

:deep(th) {
  white-space: nowrap;
}
</style>