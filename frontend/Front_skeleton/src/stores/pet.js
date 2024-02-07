import { defineStore } from 'pinia';

export const usePositionStore = defineStore('pet', {
    state: () => ({
      position: { x: 850, y: 400 },
    }),
    actions: {
      setPosition(x, y) {
        this.position.x = x;
        this.position.y = y;
      },
    },
  });