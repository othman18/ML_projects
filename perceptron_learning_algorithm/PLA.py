import random
import numpy as np
from matplotlib import pyplot as plt

random_v = random.uniform(-1, 1)
random_v2 = random.uniform(-1, 1)


def f(x):
    # y = ax + b
    return random_v * x + random_v2


# perceptron learning algorithm
class PLA:
    weights = []
    bias = 1
    learning_rate = 0.01
    done_training = 0

    def __init__(self):
        self.weights.append(self.bias)
        self.weights.append(random.uniform(-1, 1))
        self.weights.append(random.uniform(-1, 1))

    def guess(self, inputs):
        sum = 0
        for i in range(0, len(self.weights)):
            sum += inputs[i] * self.weights[i]
        return np.sign(sum)

    def train(self, inputs, target):
        guess = self.guess(inputs)
        error = target - guess
        # tune all the weights
        for i in range(0, len(self.weights)):
            self.weights[i] += error * inputs[i] * self.learning_rate
        if error != 0:
            self.done_training += 1

    def guess_y(self, x):
        # w0*x + w1*y + w2*bias = 0
        # y = -(w0*x + w2*bias) / w1
        return -(self.weights[0] * x + self.weights[2]) / self.weights[1]


class Point:
    label = None
    x = None
    y = None
    bias = 1

    def __init__(self):
        self.x = random.uniform(-1, 1)
        self.y = random.uniform(-1, 1)
        self.label = 1 if f(self.x) > self.y else -1
        self.color = 'b' if f(self.x) > self.y else 'orange'
        self.coordinate = [self.x, self.y, self.bias]


def run(n):
    perceptron = PLA()
    points = []
    for i in range(0, n):
        points.append(Point())
    counter = 0

    while True:
        perceptron.done_training = 0
        for point in points:
            perceptron.train(point.coordinate, point.label)
        # double the learning rate after a while
#        if i % 10 == 0:
#            perceptron.learning_rate *= 2
        if perceptron.done_training == 0:
            print("perceptron approximation done in %s iterations" % counter)
            break
        if counter == 1000:
            print("took too long had to break")
            counter = -1
            break
        counter += 1

    for point in points:
        if perceptron.guess(point.coordinate) != point.label:
            plt.scatter(point.x, point.y, color=point.color, edgecolors='r')
        else:
            plt.scatter(point.x, point.y, color=point.color)
    plt.plot([-1, 1], [perceptron.guess_y(-1), perceptron.guess_y(1)], 'k-', lw=2)
    if counter != -1:
        plt.title("Classification of %s points in %s iterations" % (n, counter))
    else:
        plt.title("Classification of %s points in more than 1000 iterations" % n)

    plt.show()


if __name__ == "__main__":
    run(500)
